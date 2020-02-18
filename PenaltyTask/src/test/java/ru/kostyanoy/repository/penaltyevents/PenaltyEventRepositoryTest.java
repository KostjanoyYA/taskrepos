package ru.kostyanoy.repository.penaltyevents;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.kostyanoy.entity.PenaltyEvent;
import ru.kostyanoy.testdata.TestDataProducer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PenaltyEventRepositoryTest {

    @RunWith(SpringRunner.class)

    @DataJpaTest
    @Import(PenaltyEventRepository.class)
    //@SpringBootTest

    //https://stackoverflow.com/questions/41081589/datajpatest-needing-a-class-outside-the-test/41084739#41084739
//https://mkyong.com/spring-boot/spring-boot-how-to-init-a-bean-for-testing/



    public class EmployeeRepositoryIntegrationTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private PenaltyEventRepository penaltyEventRepository;

        @Test
        public void whenFindByNames_thenReturnEvents() {
            // given
            PenaltyEvent penaltyEvent = TestDataProducer.createRandomPenaltyEvent();
            entityManager.persist(penaltyEvent);
            entityManager.flush();

            // when
            List<PenaltyEvent> found = penaltyEventRepository.find(
                    penaltyEvent.getCar().getCarOwner().getFirstName(),
                    penaltyEvent.getCar().getCarOwner().getFirstName(),
                    penaltyEvent.getCar().getCarOwner().getFirstName());

            boolean result = !found.isEmpty();
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

    }

}