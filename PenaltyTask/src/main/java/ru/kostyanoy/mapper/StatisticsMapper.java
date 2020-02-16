package ru.kostyanoy.mapper;

import org.springframework.stereotype.Component;
import ru.kostyanoy.api.dto.StatisticsDto;
import ru.kostyanoy.entity.Statistics;

@Component
public class StatisticsMapper {

    public StatisticsDto toDto(Statistics statistics) {
        return StatisticsDto.builder()
                .topPlace(statistics.getTopPlace())
                .occurrencesNumber(statistics.getOccurrencesNumber())
                .fineType(statistics.getFine().getType())
                .fineID(statistics.getFine().getId())
                .build();
    }
}
