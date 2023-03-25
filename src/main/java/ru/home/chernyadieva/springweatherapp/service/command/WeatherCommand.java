package ru.home.chernyadieva.springweatherapp.service.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import ru.home.chernyadieva.springweatherapp.repository.entity.UserEntity;
import ru.home.chernyadieva.springweatherapp.service.UserService;
import ru.home.chernyadieva.springweatherapp.service.command.interf.Command;
import ru.home.chernyadieva.springweatherapp.util.client.WeatherAPIClient;
import ru.home.chernyadieva.springweatherapp.util.client.dto.OpenWeatherDTO;
import ru.home.chernyadieva.springweatherapp.util.client.exception.UserNotFoundException;

import java.util.Objects;

@Component
public class WeatherCommand implements Command {
    private final UserService userService;
    private final TelegramBot telegramBotSender;
    private final WeatherAPIClient weatherApiClient;

    private final static String GET_WEATHER_MESSAGE = "Текущая погода:\n";
    private final static String NOT_FOUND_LOCATION_MESSAGE = "Локация не найдена! Добавь ее в закрепе геолокации в чате";

    public WeatherCommand(UserService userService, TelegramBot telegramBotSender, WeatherAPIClient weatherApiClient) {
        this.telegramBotSender = telegramBotSender;
        this.userService = userService;
        this.weatherApiClient = weatherApiClient;
    }

    /**
     * Метод предоставления текущей погоды
     *
     * @param message
     */
    @Override
    public void execute(Message message) {
        UserEntity user = userService.findByUserId(message.from().id()).orElseThrow(() ->
                new UserNotFoundException("User with this id not found in DB!"));

        if (Objects.isNull(user.getLocation())) {
            telegramBotSender.execute(new SendMessage(message.from().id(), NOT_FOUND_LOCATION_MESSAGE));
            return;
        }

        double latitude = user.getLocation().getLatitude();
        double longitude = user.getLocation().getLongitude();
        OpenWeatherDTO openWeatherDTO = weatherApiClient.getCurrentWeather(latitude, longitude);

        telegramBotSender.execute(new SendMessage(message.from().id(), GET_WEATHER_MESSAGE + openWeatherDTO.getMain()));
    }
}





