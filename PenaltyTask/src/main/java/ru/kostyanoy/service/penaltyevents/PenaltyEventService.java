package ru.kostyanoy.service.penaltyevents;

import ru.kostyanoy.api.dto.ReportDto;

import java.util.List;

public interface PenaltyEventService {

    List<ReportDto> get(String firstName, String middleName, String lastName, String fullStateNumber);
}
