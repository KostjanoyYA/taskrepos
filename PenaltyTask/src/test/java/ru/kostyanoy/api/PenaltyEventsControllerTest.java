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
import ru.kostyanoy.api.dto.PenaltyEventDto;
import ru.kostyanoy.entity.PenaltyEvent;
import ru.kostyanoy.entity.statenumbervalidator.StateNumberValidator;
import ru.kostyanoy.entity.statenumbervalidator.StateNumberValidatorRus;
import ru.kostyanoy.mapper.PenaltyEventMapper;
import ru.kostyanoy.service.penaltyevents.PenaltyEventService;
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
@WebMvcTest(PenaltyEventsController.class)
public class PenaltyEventsControllerTest {

    @TestConfiguration
    static class PenaltyEventsControllerTestContextConfiguration {

        @Autowired
        private StateNumberValidator validator;

        @Bean
        public PenaltyEventMapper penaltyEventMapper() {
            return new PenaltyEventMapper(validator);
        }

        @Bean
        public StateNumberValidator stateNumberValidator() {
            return new StateNumberValidatorRus();
        }
    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PenaltyEventService service;

    @Autowired
    private PenaltyEventMapper penaltyEventMapper;


    @Test
    public void givenPenaltyEvents_whenGetPenaltyEvents_thenReturnJsonArray() throws Exception {

        PenaltyEvent penaltyEvent = TestDataProducer.createRandomPenaltyEvent();

        List<PenaltyEventDto> penaltyEventDtoList = Stream.of(penaltyEvent)
                .map(penaltyEventMapper::toDto)
                .collect(Collectors.toList());

        given(service.get("", "",
                penaltyEvent.getCar().getCarOwner().getLastName(),
                "")).willReturn(penaltyEventDtoList);

        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add(Parameters.FIRST_NAME, "");
        map.add(Parameters.MIDDLE_NAME, "");
        map.add(Parameters.LAST_NAME, penaltyEvent.getCar().getCarOwner().getLastName());
        map.add(Parameters.FULL_STATE_NUMBER, "");

        mvc.perform(get(Paths.PENALTYEVENTS)
                .queryParams(map)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].lastName").value(penaltyEvent.getCar().getCarOwner().getLastName()));
    }
}
