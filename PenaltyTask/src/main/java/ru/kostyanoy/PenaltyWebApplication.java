package ru.kostyanoy;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.kostyanoy.configuration.LocalizationProperties;

//TODO Посмотри, пожалуйста, todo'шки В StateNumber
//TODO Код-ревью (ну, хоть одним глазком ). Правильно ли перенёс проект на maven?
//TODO "Оформить в виде Spring-Boot приложения, которое запускается командой maven.
// Архив с выполненным заданием не должен содержать скомпилированных модулей". Пока это не делал. Если будут вопросы, спрошу позже
//TODO Как сделать запуск idex.html? А то фронт написал, а как его из приложения запустить, не понятно.
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
