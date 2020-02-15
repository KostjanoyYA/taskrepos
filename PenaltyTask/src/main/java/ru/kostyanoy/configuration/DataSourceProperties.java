package ru.kostyanoy.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "datasource")
public class DataSourceProperties {

    @NotEmpty
    private String url;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotNull
    private Integer maximumPoolSize;
}
