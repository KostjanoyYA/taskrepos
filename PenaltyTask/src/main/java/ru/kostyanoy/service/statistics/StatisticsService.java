package ru.kostyanoy.service.statistics;

import ru.kostyanoy.api.dto.StatisticsDto;

import java.util.List;

public interface StatisticsService {

    List<StatisticsDto> getByTop(Long id);
}
