package ru.home.chernyadieva.springweatherapp.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import ru.home.chernyadieva.springweatherapp.service.CommandSendMessageFactory;
import ru.home.chernyadieva.springweatherapp.service.SendBotMessageService;
import ru.home.chernyadieva.springweatherapp.service.command.LocationUpdateCommand;
import ru.home.chernyadieva.springweatherapp.service.command.StartCommand;
import ru.home.chernyadieva.springweatherapp.service.command.WeatherCommand;
import ru.home.chernyadieva.springweatherapp.util.client.RestTemplateLoggingInterceptor;

import java.nio.charset.StandardCharsets;

@EnableTransactionManagement
@EnableJpaRepositories("ru.home.chernyadieva.springweatherapp.repository")
@Configuration
@PropertySource("classpath:application.yaml")
public class AppConfig {

    @Value(value = "${app.bot-token}")
    private String botToken;

    @Bean
    public SendBotMessageService messageListenerService(StartCommand startCommand,
                                                        LocationUpdateCommand locationUpdateCommand,
                                                        WeatherCommand weatherCommand,
                                                        TelegramBot telegramBotSender,
                                                        CommandSendMessageFactory factory) {
        return new SendBotMessageService(startCommand, locationUpdateCommand, weatherCommand, telegramBotSender, factory);
    }

    @Bean
    public TelegramBot telegramBotBean(SendBotMessageService sendBotMessageService) {
        TelegramBot telegramBot = new TelegramBot(botToken);
        telegramBot.setUpdatesListener(sendBotMessageService);

        return telegramBot;
    }

    @Bean
    public TelegramBot telegramBotSender() {
        TelegramBot telegramBot = new TelegramBot(botToken);
        return telegramBot;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateLoggingInterceptor restTemplateHeadersInterceptor) {
        return new RestTemplateBuilder()
                .requestFactory(SimpleClientHttpRequestFactory::new)
                .messageConverters(
                        new StringHttpMessageConverter(StandardCharsets.UTF_8),
                        new MappingJackson2HttpMessageConverter())
                .additionalInterceptors(restTemplateHeadersInterceptor)
                .build();
    }
}
