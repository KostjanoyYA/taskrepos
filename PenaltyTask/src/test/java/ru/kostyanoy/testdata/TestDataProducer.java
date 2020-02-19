package ru.kostyanoy.testdata;

import ru.kostyanoy.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

public class TestDataProducer {

    public static CarOwner createRandomCarOwner() {
        CarOwner carOwner = new CarOwner();
        carOwner.setLastName(randomAlphabetic(10));
        carOwner.setFirstName(randomAlphabetic(10));
        carOwner.setMiddleName(randomAlphabetic(10));
        return carOwner;
    }

    public static StateNumber createRandomStateNumber() {
        StateNumber stateNumber = new StateNumber();
        stateNumber.setCountry("RUS");
        stateNumber.setRegionCode(Integer.parseInt(randomNumeric(3)));

        final String literals = "АВЕКМНОРСТУХ" + "ABEKMHOPCTYX";
        Random rnd = new Random();
        StringBuilder series = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            series.append(literals.charAt(rnd.nextInt(literals.length())));
        }
        stateNumber.setSeries(series.toString());

        stateNumber.setNumber(Integer.parseInt(randomNumeric(3)));
        return stateNumber;
    }

    public static Car createRandomCar() {
        Car car = new Car();
        car.setMake(randomAlphabetic(5));
        car.setModel(randomAlphabetic(5));
        car.setStateNumber(createRandomStateNumber());
        car.setCarOwner(createRandomCarOwner());
        return car;
    }

    public static Fine createRandomFine() {
        Fine fine = new Fine();
        fine.setType(randomAlphabetic(15));
        fine.setCharge(BigDecimal.valueOf(Long.parseLong(randomNumeric(3))));
        return fine;
    }

    public static PenaltyEvent createRandomPenaltyEvent() {
        PenaltyEvent penaltyEvent = new PenaltyEvent();
        penaltyEvent.setEventDate(LocalDateTime.now());
        penaltyEvent.setCar(createRandomCar());
        penaltyEvent.setFine(createRandomFine());
        return penaltyEvent;
    }

    public static Statistics createRandomStatistics() {
        Statistics statistics = new Statistics();
        statistics.setOccurrencesNumber(Long.valueOf(randomNumeric(3)));
        statistics.setTopPlace(statistics.getOccurrencesNumber());
        statistics.setFine(createRandomFine());
        return statistics;
    }
}
