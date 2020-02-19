package ru.kostyanoy.api;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import ru.kostyanoy.api.dto.StatisticsDto;
import ru.kostyanoy.entity.Statistics;
import ru.kostyanoy.mapper.StatisticsMapper;
import ru.kostyanoy.service.statistics.StatisticsService;
import ru.kostyanoy.testdata.TestDataProducer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @TestConfiguration
    static class StatisticsControllerTestContextConfiguration {

        @Bean
        public StatisticsMapper statisticsMapper() {
            return new StatisticsMapper();
        }
    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatisticsService service;

    @Autowired
    private StatisticsMapper statisticsMapper;

    private long top = 5L;

    @Test
    public void givenStatistics_whenGetStatistics_thenReturnJsonArray() throws Exception {

        Statistics statistics = TestDataProducer.createRandomStatistics();
        statistics.setTopPlace(top);

        List<StatisticsDto> statisticsDtoList = Stream.of(statistics)
                .map(statisticsMapper::toDto)
                .collect(Collectors.toList());

        given(service.getByTop(top)).willReturn(statisticsDtoList);

        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add(Parameters.TOP, String.valueOf(top));

        mvc.perform(get(Paths.STATISTICS + "/" + top)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].topPlace").value(String.valueOf(statistics.getTopPlace())));
    }
}
