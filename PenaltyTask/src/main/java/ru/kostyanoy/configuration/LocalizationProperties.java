package ru.kostyanoy.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
@Validated
@ConfigurationProperties(prefix = "localization")
public class LocalizationProperties {

    @NotEmpty
    private String stateNumberValidator;
}
