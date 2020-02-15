package ru.kostyanoy.mapper;

import org.springframework.stereotype.Component;
import ru.kostyanoy.api.dto.AuthorDto;
import ru.kostyanoy.entity.CarOwner;

@Component
public class StatisticsMapper {

    public AuthorDto toDto(CarOwner carOwner) {
        return AuthorDto.builder()
                .id(carOwner.getId())
                .name(carOwner.getName())
                .description(carOwner.getDescription())
                .build();
    }
}
