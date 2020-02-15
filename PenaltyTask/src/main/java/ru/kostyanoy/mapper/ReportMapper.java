package ru.kostyanoy.mapper;

import org.springframework.stereotype.Component;
import ru.kostyanoy.api.dto.ReportDto;
import ru.kostyanoy.entity.Car;

@Component
public class ReportMapper {

    public ReportDto toDto(Car car) {
        return ReportDto.builder()
                .penaltyEventID(car.getId())
                .name(car.getName())
                .description(car.getDescription())
                .isbn(car.getIsbn())
                .authorId(car.getAuthor().getId())
                .build();
    }
}
