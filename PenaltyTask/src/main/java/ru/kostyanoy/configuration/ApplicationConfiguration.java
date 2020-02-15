//package ru.cft.focusstart.configuration;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableConfigurationProperties(DataSourceProperties.class)
//public class ApplicationConfiguration {
//
//    @Bean(destroyMethod = "close")
//    public HikariDataSource dataSource(DataSourceProperties dataSourceProperties) {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(dataSourceProperties.getUrl());
//        config.setUsername(dataSourceProperties.getUserName());
//        config.setPassword(dataSourceProperties.getPassword());
//        config.setMaximumPoolSize(dataSourceProperties.getMaximumPoolSize());
//        config.setDriverClassName("org.postgresql.Driver");
//
//        return new HikariDataSource(config);
//    }
//}
