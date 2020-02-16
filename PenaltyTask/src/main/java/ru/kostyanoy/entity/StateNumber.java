package ru.kostyanoy.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kostyanoy.entity.statenumbervalidator.StateNumberValidator;

import javax.persistence.*;
import java.util.Optional;

@Data
@Component
@Entity
@Table(name = "stateNumber")
public class StateNumber {

    @Autowired
    private static StateNumberValidator validator;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "regionCode")
    private int regionCode;

    @Column(name = "series")
    private String series;

    @Column(name = "number")
    private int number;

    public String getFullNumber() {
        return validator.generateFullNumber(this);
    }

    public boolean setStateNumber(String fullNumber) {
        Optional<StateNumber> parsedStateNumber = validator.parseStateNumber(fullNumber);

        if (!parsedStateNumber.isPresent()) {
            return false;
        }
        series = parsedStateNumber.get().getSeries();
        number = parsedStateNumber.get().getNumber();
        regionCode = parsedStateNumber.get().getRegionCode();
        country = parsedStateNumber.get().getCountry();

        return true;
    }

}
