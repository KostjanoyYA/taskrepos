package ru.kostyanoy;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.kostyanoy.configuration.LocalizationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ru.kostyanoy.configuration.DataSourceProperties.class, LocalizationProperties.class})
public class PenaltyWebApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PenaltyWebApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
