package ru.kostyanoy.entity.statenumbervalidator;

import ru.kostyanoy.entity.StateNumber;

import java.util.Optional;

public interface StateNumberValidator {
    Optional<StateNumber> parseStateNumber(String fullNumber);
    String generateFullNumber(StateNumber stateNumber);
}
