package ru.kostyanoy.mapper;

import org.springframework.stereotype.Component;
import ru.kostyanoy.api.dto.ReportDto;
import ru.kostyanoy.entity.PenaltyEvent;

@Component
public class ReportMapper {

    public ReportDto toDto(PenaltyEvent penaltyEvent) {
        return ReportDto.builder()
                .penaltyEventID(penaltyEvent.getId())
                .penaltyEventTimeStamp(penaltyEvent.getEventDate())
                .fineType(penaltyEvent.getFine().getType())
                .fineCharge(penaltyEvent.getFine().getCharge())
                .fullStateNumber(penaltyEvent.getCar().getStateNumber().getFullNumber())
                .carMake(penaltyEvent.getCar().getMake())
                .carModel(penaltyEvent.getCar().getModel())
                .lastName(penaltyEvent.getCar().getCarOwner().getLastName())
                .firstName(penaltyEvent.getCar().getCarOwner().getFirstName())
                .middleName(penaltyEvent.getCar().getCarOwner().getMiddleName())
                .build();
    }
}
