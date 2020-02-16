package ru.kostyanoy.service.validation;

import ru.kostyanoy.exception.InvalidParametersException;

public final class Validator {

    private Validator() {
    }

    public static void checkNotNull(String parameterName, Object value) {
        if (value == null) {
            throw new InvalidParametersException(String.format("Parameter '%s' must be specified", parameterName));
        }
    }

    public static void checkNull(String parameterName, Object value) {
        if (value != null) {
            throw new InvalidParametersException(String.format("Parameter '%s' must be not specified", parameterName));
        }
    }

    public static void checkPositive(String parameterName, Long value) {
        checkNotNull(parameterName, value);
        if (value <= 0) {
            throw new InvalidParametersException(
                    String.format(
                            "Parameter '%s' (= %d) must have positive value",
                            parameterName,
                            value
                    )
            );
        }
    }
}
