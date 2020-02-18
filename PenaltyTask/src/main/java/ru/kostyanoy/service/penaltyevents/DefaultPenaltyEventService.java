package ru.kostyanoy.service.penaltyevents;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kostyanoy.api.dto.PenaltyEventDto;
import ru.kostyanoy.entity.PenaltyEvent;
import ru.kostyanoy.entity.StateNumber;
import ru.kostyanoy.entity.statenumbervalidator.StateNumberValidator;
import ru.kostyanoy.exception.InvalidParametersException;
import ru.kostyanoy.exception.ObjectNotFoundException;
import ru.kostyanoy.mapper.PenaltyEventMapper;
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

    private final PenaltyEventMapper penaltyEventMapper;

    private final StateNumberValidator validator;

    @Override
    @Transactional(readOnly = true)
    public List<PenaltyEventDto> get(String firstName, String middleName, String lastName, String fullStateNumber) {
        Optional<StateNumber> newStateNumber = getStateNumber(fullStateNumber);
        if (newStateNumber.isPresent()) {
            List<PenaltyEvent> penaltyEvents = penaltyEventRepository.find(newStateNumber.get().getId());

            return (penaltyEvents == null || penaltyEvents.isEmpty())
                    ? new ArrayList<PenaltyEventDto>()
                    : penaltyEvents
                    .stream()
                    .map(penaltyEventMapper::toDto)
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
                .map(penaltyEventMapper::toDto)
                .collect(Collectors.toList());
    }

    private Optional<StateNumber> getStateNumber(String fullStateNumber) {
        Optional<StateNumber> stateNumber = validator.parseStateNumber(fullStateNumber);

        if (validator.parseStateNumber(fullStateNumber).isPresent()) {
            checkNull("stateNumberID", stateNumber.get().getId());
            return stateNumberRepository.find(
                    stateNumber.get().getCountry(),
                    stateNumber.get().getRegionCode(),
                    stateNumber.get().getSeries(),
                    stateNumber.get().getNumber());
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
