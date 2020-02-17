package ru.kostyanoy.repository.penaltyevents;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kostyanoy.entity.PenaltyEvent;

import java.util.List;

public interface PenaltyEventRepository extends JpaRepository<PenaltyEvent, Long> {

    String GET_QUERY =
            "select p " +
                    "from PenaltyEvent p" +
                    "join fetch p.fine " +
                    "join fetch p.car ";
    String GET_BY_OWNER_NAME_QUERY =
            GET_QUERY +
                    " where lower(p.car.carowner.lastName) like lower(concat('%', :lastName, '%')) " +
                    "  and lower(p.car.carowner.firstName) like lower(concat('%', :firstName, '%'))" +
                    "  and lower(p.car.carowner.middleName) like lower(concat('%', :middleName, '%'))";

    String GET_BY_STATENUMBER_QUERY =
            GET_QUERY +
                    " where p.car.statenumber.id = :stateNumberID";

    @Query(value = GET_BY_STATENUMBER_QUERY)
    List<PenaltyEvent> find(@Param("stateNumberID") Long stateNumberID);

    @Query(value = GET_BY_OWNER_NAME_QUERY)
    List<PenaltyEvent> find(@Param("firstName") String firstName,
                            @Param("middleName") String middleName,
                            @Param("lastName") String lastName);
}
