package ru.kostyanoy.repository.statistics;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.kostyanoy.entity.Statistics;
import ru.kostyanoy.repository.penaltyevents.PenaltyEventRepository;
import ru.kostyanoy.testdata.TestDataProducer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticsRepositoryTest {

    @RunWith(SpringRunner.class)
    @DataJpaTest
    @Import(PenaltyEventRepository.class)

    public class StatisticsRepositoryIntegrationTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private StatisticsRepository statisticsRepository;

        private long top = 5L;

        @Test
        public void whenFindByTop_thenReturnEvents() {
            // given
            for (long i = 1; i < 2 * top; i++) {
                Statistics statistics = new Statistics();
                statistics.setId(i);
                statistics.setTopPlace(i);
                statistics.setOccurrencesNumber(i);
                statistics.setFine(TestDataProducer.createRandomFine());
                statistics.getFine().setId(i);

                entityManager.persist(statistics);
                entityManager.flush();
            }

            // when
            List<Statistics> found = statisticsRepository.getByTop(top);

            boolean result = found != null && !found.isEmpty();
            result = result &&
                    found.stream().allMatch((s) -> s.getTopPlace() >= top);

            // then
            assertThat(result);
        }

    }

}