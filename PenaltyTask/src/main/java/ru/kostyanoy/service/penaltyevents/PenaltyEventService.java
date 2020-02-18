package ru.kostyanoy.service.penaltyevents;

import ru.kostyanoy.api.dto.PenaltyEventDto;

import java.util.List;

public interface PenaltyEventService {

    List<PenaltyEventDto> get(String firstName, String middleName, String lastName, String fullStateNumber);
}
