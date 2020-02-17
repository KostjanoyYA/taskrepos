package ru.kostyanoy.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kostyanoy.entity.statenumbervalidator.StateNumberValidator;
import ru.kostyanoy.entity.statenumbervalidator.StateNumberValidatorRus;

import javax.persistence.*;
import java.util.Optional;

@Data
@Component
@Entity
@Table(name = "state_number")
public class StateNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "region_code")
    private int regionCode;

    @Column(name = "series")
    private String series;

    @Column(name = "number")
    private int number;

    @Autowired
    @Transient
    private StateNumberValidator validator;

    public String getFullNumber() {
        validator = new StateNumberValidatorRus();
        //TODO здесь захардкодил, чтобы доделать.
        // В servletAPI в зависимости от значения локализации в конфиге в поле validator подтягивалась реализация
        // интерфейса. Здесь же spring должен сам подтянуть bean из ApplicationConfiguration. Bean создаётся, а экземпляр
        // нет. Из-за этого вылетают NPE.

        return validator.generateFullNumber(this);
    }

    public boolean setStateNumber(String fullNumber) {
        validator = new StateNumberValidatorRus(); //TODO здесь захардкодил, чтобы доделать. Та же проблема
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
