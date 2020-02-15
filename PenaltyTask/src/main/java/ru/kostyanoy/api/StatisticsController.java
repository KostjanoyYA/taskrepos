package ru.kostyanoy.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kostyanoy.api.dto.StatisticsDto;
import ru.kostyanoy.service.statistics.StatisticsService;

@RestController
@RequestMapping(path = Paths.STATISTICS, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping(path = Paths.TOP)
    public StatisticsDto getById(@PathVariable(name = Parameters.TOP) Long top) {
        return statisticsService.getByTop(top);
    }
}
