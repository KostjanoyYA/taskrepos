package ru.kostyanoy.mapper;

import org.springframework.stereotype.Component;
import ru.kostyanoy.api.dto.BookDto;
import ru.kostyanoy.entity.Car;

@Component
public class ReportMapper {

    public BookDto toDto(Car car) {
        return BookDto.builder()
                .id(car.getId())
                .name(car.getName())
                .description(car.getDescription())
                .isbn(car.getIsbn())
                .authorId(car.getAuthor().getId())
                .build();
    }
}
