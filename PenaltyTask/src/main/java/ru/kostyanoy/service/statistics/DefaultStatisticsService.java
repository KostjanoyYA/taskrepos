package ru.kostyanoy.service.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kostyanoy.api.dto.StatisticsDto;
import ru.kostyanoy.mapper.StatisticsMapper;
import ru.kostyanoy.repository.statistics.StatisticsRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.kostyanoy.service.validation.Validator.checkPositive;

@Service
@RequiredArgsConstructor
public class DefaultStatisticsService implements StatisticsService {

    private final StatisticsRepository statisticsRepository;

    private final StatisticsMapper statisticsMapper;

    @Override
    @Transactional
    public List<StatisticsDto> getByTop(Long top) {
        checkPositive("top", top);

        return statisticsRepository.getByTop(top)
                .stream()
                .map(statisticsMapper::toDto)
                .collect(Collectors.toList());
    }

}
