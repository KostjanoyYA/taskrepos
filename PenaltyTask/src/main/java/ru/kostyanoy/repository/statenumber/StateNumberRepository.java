package ru.kostyanoy.repository.statenumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kostyanoy.entity.StateNumber;
import ru.kostyanoy.service.penaltyevents.DefaultPenaltyEventService;

import java.util.Optional;

public interface StateNumberRepository extends JpaRepository<StateNumber, Long> {

    String GET_QUERY =
            "select s " +
                    "from StateNumber s " +
                    "where s.country = :country " +
                    "and s.regionCode = :regionCode " +
                    "and s.series = :series " +
                    "and s.number = :number";
    Logger log = LoggerFactory.getLogger(DefaultPenaltyEventService.class);

    @Query(value = GET_QUERY)
    Optional<StateNumber> find(
            @Param("country") String country,
            @Param("regionCode") int regionCode,
            @Param("series") String series,
            @Param("number") int number);
}