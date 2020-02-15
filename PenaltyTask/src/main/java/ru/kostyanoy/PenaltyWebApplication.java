package ru.kostyanoy;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PenaltyWebApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PenaltyWebApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
