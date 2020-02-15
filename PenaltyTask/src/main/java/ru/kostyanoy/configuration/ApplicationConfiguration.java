package ru.kostyanoy.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kostyanoy.entity.StateNumberValidator;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class ApplicationConfiguration {

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource(ru.kostyanoy.configuration.DataSourceProperties dataSourceProperties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dataSourceProperties.getUrl());
        config.setUsername(dataSourceProperties.getUsername());
        config.setPassword(dataSourceProperties.getPassword());
        config.setMaximumPoolSize(dataSourceProperties.getMaximumPoolSize());
        config.setDriverClassName("org.postgresql.Driver");

        return new HikariDataSource(config);
    }

    @Bean //TODO Далее
    public StateNumberValidator getValidator(ru.kostyanoy.configuration.DataSourceProperties dataSourceProperties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dataSourceProperties.getUrl());
        config.setUsername(dataSourceProperties.getUsername());
        config.setPassword(dataSourceProperties.getPassword());
        config.setMaximumPoolSize(dataSourceProperties.getMaximumPoolSize());
        config.setDriverClassName("org.postgresql.Driver");

        return new HikariDataSource(config);
    }
}
