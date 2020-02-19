package ru.kostyanoy.services.penaltyevents;

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
import ru.kostyanoy.api.dto.PenaltyEventDto;
import ru.kostyanoy.entity.PenaltyEvent;
import ru.kostyanoy.entity.statenumbervalidator.StateNumberValidator;
import ru.kostyanoy.entity.statenumbervalidator.StateNumberValidatorRus;
import ru.kostyanoy.mapper.PenaltyEventMapper;
import ru.kostyanoy.repository.penaltyevents.PenaltyEventRepository;
import ru.kostyanoy.repository.statenumber.StateNumberRepository;
import ru.kostyanoy.service.penaltyevents.DefaultPenaltyEventService;
import ru.kostyanoy.service.penaltyevents.PenaltyEventService;
import ru.kostyanoy.testdata.TestDataProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PenaltyEventServiceTest {

    @TestConfiguration
    class PenaltyEventServiceImplTestContextConfiguration {

        @Bean
        public PenaltyEventService penaltyEventService() {
            return new DefaultPenaltyEventService(
                    penaltyEventRepository,
                    stateNumberRepository,
                    penaltyEventMapper,
                    validator);
        }

        @Bean
        public StateNumberValidator stateNumberValidator() {
            return new StateNumberValidatorRus();
        }

        @Bean
        public PenaltyEventMapper penaltyEventMapper() {
            return new PenaltyEventMapper(validator);
        }
    }

    @Autowired
    private PenaltyEventService penaltyEventService;

    @MockBean
    private PenaltyEventRepository penaltyEventRepository;

    @MockBean
    private StateNumberRepository stateNumberRepository;

    @Autowired
    private PenaltyEventMapper penaltyEventMapper;

    @Autowired
    private StateNumberValidator validator;

    private long stateNumberID = 5L;

    PenaltyEvent penaltyEvent;

    List<PenaltyEvent> expectedList;

    @BeforeEach
    public void setUp() {
        expectedList = new ArrayList<>();
        penaltyEvent = TestDataProducer.createRandomPenaltyEvent();
        penaltyEvent.getCar().getStateNumber().setId(stateNumberID);
        expectedList.add(penaltyEvent);

        Mockito.when(penaltyEventRepository.find(penaltyEvent.getCar().getStateNumber().getId()))
                .thenReturn(expectedList);

        Mockito.when(penaltyEventRepository.find(
                "",
                "",
                penaltyEvent.getCar().getCarOwner().getLastName()))
                .thenReturn(expectedList);

        Mockito.when(stateNumberRepository.find(
                penaltyEvent.getCar().getStateNumber().getCountry(),
                penaltyEvent.getCar().getStateNumber().getRegionCode(),
                penaltyEvent.getCar().getStateNumber().getSeries(),
                penaltyEvent.getCar().getStateNumber().getNumber()))
                .thenReturn(Optional.ofNullable(penaltyEvent.getCar().getStateNumber()));
    }


    @Test
    public void whenValidStateNumber_thenPenaltyEventsWouldBeFound() {
        List<PenaltyEventDto> expected = expectedList
                .stream()
                .map(penaltyEventMapper::toDto)
                .collect(Collectors.toList());

        List<PenaltyEventDto> found = penaltyEventService.get("", "", "",
                validator.generateFullNumber(penaltyEvent.getCar().getStateNumber()));

        assertThat(found)
                .isEqualTo(expected);
    }

    @Test
    public void whenValidLastNameAndEmptyFullStateNumber_thenPenaltyEventsWouldBeFound() {
        List<PenaltyEventDto> expected = expectedList
                .stream()
                .map(penaltyEventMapper::toDto)
                .collect(Collectors.toList());

        List<PenaltyEventDto> found = penaltyEventService.get(
                "",
                "",
                penaltyEvent.getCar().getCarOwner().getLastName(),
                "");

        assertThat(found)
                .isEqualTo(expected);
    }

}