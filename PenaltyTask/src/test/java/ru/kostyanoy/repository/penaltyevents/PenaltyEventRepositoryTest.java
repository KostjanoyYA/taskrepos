package ru.kostyanoy.repository.penaltyevents;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.kostyanoy.entity.PenaltyEvent;
import ru.kostyanoy.entity.StateNumber;
import ru.kostyanoy.entity.statenumbervalidator.StateNumberValidator;
import ru.kostyanoy.testdata.TestDataProducer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PenaltyEventRepositoryTest {

    @RunWith(SpringRunner.class)
    @DataJpaTest
    @Import(PenaltyEventRepository.class)

    public class PenaltyEventRepositoryIntegrationTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private PenaltyEventRepository penaltyEventRepository;

        @Autowired
        private StateNumberValidator validator;

        @Test
        public void whenFindByNames_thenReturnEvents() {
            // given
            int copyNumber = 5;
            PenaltyEvent penaltyEvent = TestDataProducer.createRandomPenaltyEvent();

            for (int i = 0; i < copyNumber; i++) {
                entityManager.persist(penaltyEvent);
                entityManager.persist(TestDataProducer.createRandomPenaltyEvent());
                entityManager.flush();
            }

            // when
            List<PenaltyEvent> found = penaltyEventRepository.find(
                    penaltyEvent.getCar().getCarOwner().getFirstName(),
                    penaltyEvent.getCar().getCarOwner().getFirstName(),
                    penaltyEvent.getCar().getCarOwner().getFirstName());

            boolean result = found != null && !found.isEmpty() && (found.size() == copyNumber);
            result = result &&
                    found.stream().allMatch((s) -> s.getCar().getCarOwner().getLastName()
                            .equals(penaltyEvent.getCar().getCarOwner().getLastName())) &&
                    found.stream().allMatch((s) -> s.getCar().getCarOwner().getFirstName()
                            .equals(penaltyEvent.getCar().getCarOwner().getFirstName())) &&
                    found.stream().allMatch((s) -> s.getCar().getCarOwner().getMiddleName()
                            .equals(penaltyEvent.getCar().getCarOwner().getMiddleName()));

            // then
            assertThat(result);
        }

        @Test
        public void whenFindByStateNumberID_thenReturnEvents() {
            // given
            long copyNumber = 5;
            PenaltyEvent penaltyEvent = TestDataProducer.createRandomPenaltyEvent();
            penaltyEvent.getCar().getStateNumber().setId(copyNumber);
            StateNumber stateNumber = penaltyEvent.getCar().getStateNumber();
            StateNumber rndNumber;

            for (int i = 0; i < copyNumber; i++) {
                entityManager.persist(penaltyEvent);
                rndNumber = TestDataProducer.createRandomStateNumber();
                rndNumber.setId((long) i);
                PenaltyEvent rndEvent = TestDataProducer.createRandomPenaltyEvent();
                rndEvent.getCar().setStateNumber(rndNumber);
                entityManager.persist(rndEvent);
                entityManager.flush();
            }

            // when
            List<PenaltyEvent> found = penaltyEventRepository.find(stateNumber.getId());

            boolean result = found != null && !found.isEmpty() && (found.size() == copyNumber);
            result = result &&
                    found.stream().allMatch((s) -> s.getCar().getStateNumber().getId()
                            .equals(penaltyEvent.getCar().getStateNumber().getId()));

            // then
            assertThat(result);
        }

    }

}