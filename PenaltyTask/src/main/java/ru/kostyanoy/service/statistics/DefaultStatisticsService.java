package ru.kostyanoy.service.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kostyanoy.api.dto.ReportDto;
import ru.kostyanoy.api.dto.StatisticsDto;
import ru.kostyanoy.entity.CarOwner;
import ru.kostyanoy.exception.ObjectNotFoundException;
import ru.kostyanoy.mapper.ReportMapper;
import ru.kostyanoy.mapper.StatisticsMapper;
import ru.kostyanoy.repository.carowner.CarOwnerRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.kostyanoy.service.validation.Validator.*;

@Service
@RequiredArgsConstructor
public class DefaultStatisticsService implements StatisticsService {

    private final CarOwnerRepository carOwnerRepository;

    private final StatisticsMapper statisticsMapper;

    private final ReportMapper reportMapper;

    @Override
    @Transactional
    public StatisticsDto create(StatisticsDto statisticsDto) {
        validate(statisticsDto);

        CarOwner carOwner = add(null, statisticsDto);

        return statisticsMapper.toDto(carOwner);
    }

    @Override
    @Transactional(readOnly = true)
    public StatisticsDto getByTop(Long id) {
        checkNotNull("id", id);

        CarOwner carOwner = getAuthor(id);

        return statisticsMapper.toDto(carOwner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatisticsDto> get(String name) {
        return carOwnerRepository.findByNameContainingIgnoreCase(name == null ? "" : name)
                .stream()
                .map(statisticsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDto> getBooks(Long id) {
        checkNotNull("id", id);

        return Optional.ofNullable(getAuthor(id).getBooks())
                .orElse(Collections.emptyList())
                .stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StatisticsDto merge(Long id, StatisticsDto statisticsDto) {
        checkNotNull("id", id);
        validate(statisticsDto);

        CarOwner carOwner = carOwnerRepository.findById(id)
                .map(existing -> update(existing, statisticsDto))
                .orElseGet(() -> add(id, statisticsDto));

        return statisticsMapper.toDto(carOwner);
    }

    private void validate(StatisticsDto statisticsDto) {
        checkNull("author.id", statisticsDto.getFineID());
        checkSize("author.name", statisticsDto.getName(), 1, 256);
        checkSize("author.description", statisticsDto.getDescription(), 1, 4096);
    }

    private CarOwner add(Long id, StatisticsDto statisticsDto) {
        CarOwner carOwner = new CarOwner();
        carOwner.setId(id);
        carOwner.setName(statisticsDto.getName());
        carOwner.setDescription(statisticsDto.getDescription());

        return carOwnerRepository.save(carOwner);
    }

    private CarOwner getAuthor(Long id) {
        return carOwnerRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("CarOwner with id %s not found", id)));
    }

    private CarOwner update(CarOwner carOwner, StatisticsDto statisticsDto) {
        carOwner.setName(statisticsDto.getName());
        carOwner.setDescription(statisticsDto.getDescription());

        return carOwner;
    }
}
