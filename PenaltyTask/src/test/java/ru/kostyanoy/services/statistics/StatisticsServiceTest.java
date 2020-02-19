package ru.kostyanoy.services.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.kostyanoy.api.dto.StatisticsDto;
import ru.kostyanoy.entity.Statistics;
import ru.kostyanoy.mapper.StatisticsMapper;
import ru.kostyanoy.repository.statistics.StatisticsRepository;
import ru.kostyanoy.service.statistics.DefaultStatisticsService;
import ru.kostyanoy.service.statistics.StatisticsService;
import ru.kostyanoy.testdata.TestDataProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisticsServiceTest {

    @TestConfiguration
    class StatisticsServiceImplTestContextConfiguration {

        @Bean
        public StatisticsService statisticsService() {
            return new DefaultStatisticsService(statisticsRepository, statisticsMapper);
        }

        @Bean
        public StatisticsMapper statisticsMapper() {
            return new StatisticsMapper();
        }
    }

    @Autowired
    private StatisticsService statisticsService;

    @MockBean
    private StatisticsRepository statisticsRepository;

    @Autowired
    private StatisticsMapper statisticsMapper;

    private long top = 5L;

    Statistics statistics;

    List<Statistics> expectedList;

    @BeforeEach
    public void setUp() {
        expectedList = new ArrayList<>();
        for (long i = 1; i < top; i++) {
            Statistics statistics = new Statistics();
            statistics.setId(i);
            statistics.setTopPlace(i);
            statistics.setOccurrencesNumber(i);
            statistics.setFine(TestDataProducer.createRandomFine());
            statistics.getFine().setId(i);
            expectedList.add(statistics);
        }

        Mockito.when(statisticsRepository.getByTop(top))
                .thenReturn(expectedList);
    }

    @Test
    public void whenValidTop_thenStatisticsWouldBeFound() {
        List<StatisticsDto> found = statisticsService.getByTop(top);

        List<StatisticsDto> expected = expectedList
                .stream()
                .map(statisticsMapper::toDto)
                .collect(Collectors.toList());

        assertThat(found)
                .isEqualTo(expected);
    }
}