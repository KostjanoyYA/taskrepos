package ru.kostyanoy.entity.statenumbervalidator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.kostyanoy.entity.StateNumber;

import java.util.stream.Stream;

class StateNumberValidatorRusTest {

    private static StateNumber stateNumber1;
    private static String fullStateNumber1;

    private static StateNumber stateNumber2;
    private static String fullStateNumber2;

    private static StateNumber stateNumber3;
    private static String fullStateNumber3;

    static {
        stateNumber1 = new StateNumber();
        stateNumber1.setCountry("RUS");
        stateNumber1.setRegionCode(1);
        stateNumber1.setSeries("ABC");
        stateNumber1.setNumber(1);
        fullStateNumber1 = "A001BC001RUS";

        stateNumber2 = new StateNumber();
        stateNumber2.setCountry("RUS");
        stateNumber2.setRegionCode(10);
        stateNumber2.setSeries("XYO");
        stateNumber2.setNumber(10);
        fullStateNumber2 = "X010YO010RUS";

        stateNumber3 = new StateNumber();
        stateNumber3.setCountry("RUS");
        stateNumber3.setRegionCode(100);
        stateNumber3.setSeries("кмн"); //Russian letters
        stateNumber3.setNumber(100);
        fullStateNumber3 = "К100МН100RUS";
    }

    private static Stream<Object[]> stateNumberStream() {
        return Stream.of(
                new Object[]{stateNumber1, fullStateNumber1},
                new Object[]{stateNumber2, fullStateNumber2},
                new Object[]{stateNumber3, fullStateNumber3}
        );
    }

    private static Stream<Object[]> fullStateNumberStream() {
        return Stream.of(
                new Object[]{fullStateNumber1, stateNumber1},
                new Object[]{fullStateNumber2, stateNumber2},
                new Object[]{fullStateNumber2, stateNumber2}
        );
    }

    @ParameterizedTest
    @MethodSource("fullStateNumberStream")
    void parseStateNumber(String input, StateNumber expected) {
        Assertions.assertEquals(expected, new StateNumberValidatorRus().parseStateNumber(input).get());
    }

    @ParameterizedTest
    @MethodSource("stateNumberStream")
    void generateFullNumber(StateNumber input, String expected) {
        Assertions.assertEquals(expected, new StateNumberValidatorRus().generateFullNumber(input));
    }
}