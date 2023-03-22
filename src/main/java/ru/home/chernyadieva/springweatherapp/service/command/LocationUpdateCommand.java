package ru.home.chernyadieva.springweatherapp.service.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import ru.home.chernyadieva.springweatherapp.repository.entity.LocationEntity;
import ru.home.chernyadieva.springweatherapp.repository.entity.UserEntity;
import ru.home.chernyadieva.springweatherapp.service.LocationService;
import ru.home.chernyadieva.springweatherapp.service.UserService;
import ru.home.chernyadieva.springweatherapp.service.command.interf.Command;
import ru.home.chernyadieva.springweatherapp.util.client.WeatherApiClient;
import ru.home.chernyadieva.springweatherapp.util.client.exception.PersonNotFoundException;

import java.util.Objects;

@Component
public class LocationUpdateCommand implements Command {
    private final UserService userService;
    private final TelegramBot telegramBotSender;
    private final WeatherApiClient weatherApiClient;
    private final LocationService locationService;

    public final static String LOCATION_UPDATE_MESSAGE = "Локация обновлена! \n " +
            "Нажимай /getWeather, чтобы узнать текущую погоду.\n";

    public LocationUpdateCommand(UserService userService, TelegramBot telegramBotSender, WeatherApiClient weatherApiClient, LocationService locationService) {
        this.telegramBotSender = telegramBotSender;
        this.userService = userService;
        this.weatherApiClient = weatherApiClient;
        this.locationService = locationService;
    }

    @Override
    public void execute (Message message, Location location){
        if (Objects.isNull(location)){
            return;
        }

        var longitude = location.longitude();
        var latitude = location.latitude();
        var userId = message.from().id();

        UserEntity user = userService.findByUserId(userId).orElseThrow(() -> new PersonNotFoundException("User not found in DB"));

        locationService.findByUserId(userId).orElseGet(() -> {
            LocationEntity loc = new LocationEntity();
            loc.setLongitude(longitude);
            loc.setLatitude(latitude);
            loc.setUser(user);

            locationService.saveLocation(loc);

            return loc;
        });

        telegramBotSender.execute(new SendMessage(userId, LOCATION_UPDATE_MESSAGE));
    }
}
