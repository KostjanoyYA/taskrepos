package ru.kostyanoy.repository.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kostyanoy.entity.Statistics;

import java.util.List;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    String GET_QUERY =
            "select s " +
                    "from Statistics s " +
                    "join fetch s.fine ";

    String GET_BY_TOP_QUERY =
            GET_QUERY +
                    " where s.topPlace <= :top";

    @Query(value = GET_BY_TOP_QUERY)
    List<Statistics> getByTop(@Param("top") Long top);
}
