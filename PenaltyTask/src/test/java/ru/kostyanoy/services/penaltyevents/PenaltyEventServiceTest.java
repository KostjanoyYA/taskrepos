package ru.kostyanoy.services.penaltyevents;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.kostyanoy.repository.penaltyevents.PenaltyEventRepository;

public class PenaltyEventServiceTest {

//    @Test
//    public void whenGetPenaltyEventByNumber_thenOK() {
//        PenaltyEvent penaltyEvent = ru.kostyanoy.testdata.TestDataProducer.createRandomPenaltyEvent();
//        //createBookAsUri(penaltyEvent);
//        Response response = RestAssured.get(
//                ru.kostyanoy.testdata.TestDataProducer.API_ROOT +
//                        ru.kostyanoy.testdata.TestDataProducer.PENALTYEVENTS);
//
//        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
//        assertTrue(response.as(List.class).size() > 0);
//    }

    @RunWith(SpringRunner.class)
    @DataJpaTest
    public class EmployeeRepositoryIntegrationTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private PenaltyEventRepository penaltyEventRepository;

        @Test
        public void whenFindByNames_thenReturnEvents() {
            // given
            Employee alex = new Employee("alex");
            entityManager.persist(alex);
            entityManager.flush();

            // when
            Employee found = employeeRepository.findByName(alex.getName());

            // then
            assertThat(found.getName())
                    .isEqualTo(alex.getName());
        }

    }

}