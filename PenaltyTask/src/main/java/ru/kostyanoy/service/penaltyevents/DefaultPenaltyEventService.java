package ru.kostyanoy.service.penaltyevents;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kostyanoy.api.dto.ReportDto;
import ru.kostyanoy.entity.PenaltyEvent;
import ru.kostyanoy.entity.StateNumber;
import ru.kostyanoy.exception.InvalidParametersException;
import ru.kostyanoy.exception.ObjectNotFoundException;
import ru.kostyanoy.mapper.ReportMapper;
import ru.kostyanoy.repository.penaltyevents.PenaltyEventRepository;
import ru.kostyanoy.repository.statenumber.StateNumberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.kostyanoy.service.validation.Validator.checkNotNull;
import static ru.kostyanoy.service.validation.Validator.checkNull;

@Service
@RequiredArgsConstructor
public class DefaultPenaltyEventService implements PenaltyEventService {

    private final PenaltyEventRepository penaltyEventRepository;

    private final StateNumberRepository stateNumberRepository;

    private final ReportMapper reportMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ReportDto> get(String firstName, String middleName, String lastName, String fullStateNumber) {
        Optional<StateNumber> newStateNumber = getStateNumber(fullStateNumber);
        if (newStateNumber.isPresent()) {
            List<PenaltyEvent> penaltyEvents = penaltyEventRepository.find(newStateNumber.get().getId());

            return (penaltyEvents == null || penaltyEvents.isEmpty())
                    ? new ArrayList<ReportDto>()
                    : penaltyEvents
                    .stream()
                    .map(reportMapper::toDto)
                    .collect(Collectors.toList());
        }

        checkNotNull("lastName", lastName);
        checkNotNull("firstName", firstName);
        checkNotNull("middleName", middleName);
        if (lastName.isEmpty()) {
            throw new InvalidParametersException(String.format("Cannot find by the state number %s and entered owner last name",
                    fullStateNumber));
        }

        return getPenaltyEventsByOwnerName(firstName, middleName, lastName)
                .stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }

    private Optional<StateNumber> getStateNumber(String fullStateNumber) {
        StateNumber stateNumber = new StateNumber();
        if (stateNumber.setStateNumber(fullStateNumber)) {
            checkNull("stateNumberID", stateNumber.getId());
            return stateNumberRepository.findStateNumberByCountryIgnoreCaseAndRegionCodeAndSeriesIgnoreCaseAndNumber(
                    stateNumber.getCountry(),
                    stateNumber.getRegionCode(),
                    stateNumber.getSeries(),
                    stateNumber.getNumber());
        }
        return Optional.empty();
    }

    private List<PenaltyEvent> getPenaltyEventsByOwnerName(String firstName, String middleName, String lastName) {
        List<PenaltyEvent> penaltyEvents = penaltyEventRepository.find(firstName, middleName, lastName);
        if (penaltyEvents == null || penaltyEvents.isEmpty()) {
            throw new ObjectNotFoundException(String.format("PenaltyEvent for car owner %s %s %s has not found",
                    firstName, middleName, lastName));
        }
        return penaltyEvents;
    }
}
