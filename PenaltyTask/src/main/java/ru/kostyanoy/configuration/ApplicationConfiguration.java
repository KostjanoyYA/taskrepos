package ru.kostyanoy.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.kostyanoy.entity.statenumbervalidator.StateNumberValidator;
import ru.kostyanoy.entity.statenumbervalidator.StateNumberValidatorRus;

@Configuration
@ComponentScan
//@ComponentScan(basePackages = {"ru.kostyanoy.entity.statenumbervalidator", "ru.kostyanoy.configuration"})
public class ApplicationConfiguration {

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource(DataSourceProperties dataSourceProperties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dataSourceProperties.getUrl());
        config.setUsername(dataSourceProperties.getUsername());
        config.setPassword(dataSourceProperties.getPassword());
        //TODO config.setMaximumPoolSize(dataSourceProperties.getMaximumPoolSize());
        config.setDriverClassName("org.postgresql.Driver");

        return new HikariDataSource(config);
    }

    @Bean
    public StateNumberValidator getValidator(ru.kostyanoy.configuration.LocalizationProperties localizationProperties) {
        String property = localizationProperties.getStateNumberValidator();
        if (property.equals("rus")) {
            return new StateNumberValidatorRus();
        } else {
            throw new RuntimeException("Invalid stateNumberValidator property: " + property);
        }
    }
}
