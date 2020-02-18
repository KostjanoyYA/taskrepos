package ru.kostyanoy;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.kostyanoy.configuration.LocalizationProperties;

//TODO "Оформить в виде Spring-Boot приложения, которое запускается командой maven.
// Архив с выполненным заданием не должен содержать скомпилированных модулей".
//CD \D C:\Programming\JReposWork\centralbankrepos\PenaltyTask
//mvn install
//mvn exec:java -Dexec.mainClass="kostyanoy.PenaltyWebApplication"
//TODO Мне надо написать тесты. На что в моём проекте их писать? Какие особенности в spring по их написанию?
//TODO JavaDoc (это я сам, просто чтоб не забыть)

@SpringBootApplication
@EnableConfigurationProperties({DataSourceProperties.class, LocalizationProperties.class})
public class PenaltyWebApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PenaltyWebApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
