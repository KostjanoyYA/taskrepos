package ru.kostyanoy.repository.penaltyevents;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kostyanoy.entity.PenaltyEvent;

import java.util.List;

public interface PenaltyEventRepository extends JpaRepository<PenaltyEvent, Long> {

// TODO переделать на fetch? Возможно ументшит длину запроса
//  String GET_QUERY_EXAMPLE =
//            "select b " +
//                    "from Car b join fetch b.author " +
//                    "where lower(b.name) like lower(concat('%', :name, '%')) " +
//                    "  and lower(b.author.name) like lower(concat('%', :authorName, '%'))";

    String GET_QUERY =
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
                    "join fine on fine.id = p.fineID";

    String GET_BY_OWNER_NAME_QUERY =
            GET_QUERY +
                    " where lower(carOwner.firstName) like lower('%' || :firstName || '%')" +
                    " and lower(carOwner.middleName) like lower('%' || :middleName || '%')" +
                    " and lower(carOwner.lastName) like lower('%' || :lastName || '%')";

    String GET_BY_STATENUMBER_QUERY =
            GET_QUERY +
                    " where stateNumber.id = :stateNumberID";

    @Query(value = GET_BY_STATENUMBER_QUERY, nativeQuery = true)
    List<PenaltyEvent> find(@Param("stateNumberID") Long stateNumberID);

    @Query(value = GET_BY_OWNER_NAME_QUERY, nativeQuery = true)
    List<PenaltyEvent> find(@Param("firstName") String firstName,
                        @Param("middleName") String middleName,
                        @Param("lastName") String lastName);
}
