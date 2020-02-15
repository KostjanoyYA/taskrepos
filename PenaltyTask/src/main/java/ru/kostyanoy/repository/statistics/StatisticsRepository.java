package ru.kostyanoy.repository.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kostyanoy.entity.PenaltyEvent;
import ru.kostyanoy.entity.Statistics;

import java.util.List;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    String DROP_TEMP_TABLES_QUERY =
            "DROP TABLE IF EXISTS temp_table; " +
                    "DROP TABLE IF EXISTS temp_count_table; " +
                    "DROP TABLE IF EXISTS temp_rang_table; ";

    String CREATE_TEMP_TABLES_QUERY =
            "CREATE TEMP TABLE temp_table AS " +
                    "select p.id                    penaltyEvent_id, " +
                    "       p.eventDate             penaltyEvent_eventDate," +
                    "       fine.id                 fine_id," +
                    "       fine.type               fine_type," +
                    "       fine.charge             fine_charge," +
                    "       carOwner.id             carOwner_id," +
                    "       carOwner.lastName       carOwner_lastName," +
                    "       carOwner.firstName      carOwner_firstName," +
                    "       carOwner.middleName     carOwner_middleName," +
                    "       car.id                  car_id," +
                    "       car.make                car_make," +
                    "       car.model               car_model," +
                    "       stateNumber.id          stateNumber_id," +
                    "       stateNumber.number      stateNumber_number," +
                    "       stateNumber.series      stateNumber_series," +
                    "       stateNumber.regionCode  stateNumber_regionCode," +
                    "       stateNumber.country     stateNumber_country " +
                    "from penaltyEvent p " +
                    "join car on car.id = p.carID " +
                    "join stateNumber on stateNumber.id = car.stateNumberID " +
                    "join carOwner on carOwner.id = car.carOwnerID " +
                    "join fine on fine.id = p.fineID; " +

                    "CREATE TEMP TABLE temp_count_table AS " +
                    "SELECT temp_table.fine_id, COUNT(*) cnt " +
                    "FROM temp_table " +
                    "GROUP BY fine_id " +
                    "ORDER BY cnt DESC; " +

                    "CREATE TEMP TABLE temp_rang_table AS " +
                    "select * from " +
                    "  (select *, dense_rank() over (order by cnt desc) as fine_top_place " +
                    "  from temp_count_table) subquery " +
                    "where fine_top_place <= :top ; " +

                    "SELECT fine.id as fine_id, fine.type as fine_type, " +
                    "fine.charge as fine_charge, temp_rang_table.fine_top_place, " +
                    "temp_rang_table.cnt as fine_occurrences " +
                    "FROM fine, temp_rang_table " +
                    "WHERE fine.id = temp_rang_table.fine_id " +
                    "ORDER BY temp_rang_table.fine_top_place ASC;";

    @Query(value = DROP_TEMP_TABLES_QUERY + CREATE_TEMP_TABLES_QUERY, nativeQuery = true)
    List<PenaltyEvent> getByTop(@Param("top") Long top);
}
