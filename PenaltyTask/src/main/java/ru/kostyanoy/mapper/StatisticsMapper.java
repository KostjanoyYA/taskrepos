package ru.kostyanoy.mapper;

import org.springframework.stereotype.Component;
import ru.kostyanoy.api.dto.StatisticsDto;
import ru.kostyanoy.entity.CarOwner;

@Component
public class StatisticsMapper {

    public StatisticsDto toDto(CarOwner carOwner) {
        return StatisticsDto.builder()
                .fineID(carOwner.getId())
                .name(carOwner.getName())
                .description(carOwner.getDescription())
                .build();
    }
}
