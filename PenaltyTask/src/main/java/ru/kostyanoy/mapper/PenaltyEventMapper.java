package ru.kostyanoy.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.kostyanoy.api.dto.PenaltyEventDto;
import ru.kostyanoy.entity.PenaltyEvent;
import ru.kostyanoy.entity.statenumbervalidator.StateNumberValidator;

@Data
@Component
public class PenaltyEventMapper {

    private final StateNumberValidator validator;

    public PenaltyEventDto toDto(PenaltyEvent penaltyEvent) {
        return PenaltyEventDto.builder()
                .penaltyEventID(penaltyEvent.getId())
                .penaltyEventTimeStamp(penaltyEvent.getEventDate())
                .fineType(penaltyEvent.getFine().getType())
                .fineCharge(penaltyEvent.getFine().getCharge())
                .fullStateNumber(validator.generateFullNumber(penaltyEvent.getCar().getStateNumber()))
                .carMake(penaltyEvent.getCar().getMake())
                .carModel(penaltyEvent.getCar().getModel())
                .lastName(penaltyEvent.getCar().getCarOwner().getLastName())
                .firstName(penaltyEvent.getCar().getCarOwner().getFirstName())
                .middleName(penaltyEvent.getCar().getCarOwner().getMiddleName())
                .build();
    }
}
