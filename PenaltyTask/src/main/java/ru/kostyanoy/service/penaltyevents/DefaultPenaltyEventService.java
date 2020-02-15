package ru.kostyanoy.service.penaltyevents;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kostyanoy.api.dto.ReportDto;
import ru.kostyanoy.entity.Car;
import ru.kostyanoy.entity.CarOwner;
import ru.kostyanoy.entity.StateNumberValidator;
import ru.kostyanoy.exception.InvalidParametersException;
import ru.kostyanoy.exception.ObjectNotFoundException;
import ru.kostyanoy.mapper.ReportMapper;
import ru.kostyanoy.repository.car.CarRepository;
import ru.kostyanoy.repository.carowner.CarOwnerRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kostyanoy.service.validation.Validator.*;

@Service
@RequiredArgsConstructor
public class DefaultPenaltyEventService implements PenaltyEventService {

    private final CarOwnerRepository carOwnerRepository;

    private final CarRepository carRepository;

    private final ReportMapper reportMapper;


    private static StateNumberValidator setLocalization() {
        Properties properties = new Properties();
        try (InputStream propertiesStream = DefaultPenaltyEventService.class.getResourceAsStream(CONFIG_FILE_NAME)) {
            properties.load(propertiesStream);
        } catch (IOException e) {
            log.error("Could not load properties", e);
            throw new RuntimeException(e);
        }
        if (properties.getProperty("stateNumberValidator") == null) {
            throw new RuntimeException("StateNumberValidator property has not specified");
        }

        if (properties.getProperty("stateNumberValidator").equals("rus")) {
            return new StateNumberValidatorRus();
        } else {
            throw new RuntimeException("Invalid stateNumberValidator property: " + properties.getProperty("stateNumberValidator"));
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<ReportDto> get(String name, String authorName) {
        return carRepository.find(name == null ? "" : name, authorName == null ? "" : authorName)
                .stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }

    private void validate(ReportDto reportDto) {
        checkNull("book.id", reportDto.getPenaltyEventID());
        checkSize("book.name", reportDto.getName(), 1, 256);
        checkSize("book.description", reportDto.getDescription(), 1, 4096);
        checkSize("book.isbn", reportDto.getIsbn(), 1, 64);
        checkNotNull("book.authorId", reportDto.getAuthorId());
    }

    private CarOwner getAuthor(Long id) {
        return carOwnerRepository.findById(id)
                .orElseThrow(() -> new InvalidParametersException(String.format("CarOwner with id %s not found", id)));
    }

    private Car getBook(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Car with id %s not found", id)));
    }
}
