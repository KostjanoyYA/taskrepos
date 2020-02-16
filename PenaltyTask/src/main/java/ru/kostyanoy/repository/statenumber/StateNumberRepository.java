package ru.kostyanoy.repository.statenumber;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kostyanoy.entity.StateNumber;

import java.util.Optional;

public interface StateNumberRepository extends JpaRepository<StateNumber, Long> {

    Optional<StateNumber> findStateNumberByCountryIgnoreCaseAndRegionCodeAndSeriesIgnoreCaseAndNumber(String country,
                                                                                                      int regionCode,
                                                                                                      String series,
                                                                                                      int number);
}