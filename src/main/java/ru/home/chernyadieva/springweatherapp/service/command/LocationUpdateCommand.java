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
import ru.home.chernyadieva.springweatherapp.util.client.WeatherAPIClient;
import ru.home.chernyadieva.springweatherapp.util.client.dto.OpenWeatherDTO;
import ru.home.chernyadieva.springweatherapp.util.client.exception.UserNotFoundException;

import java.util.Objects;

@Component
public class LocationUpdateCommand implements Command {
    private final UserService userService;
    private final TelegramBot telegramBotSender;
    private final WeatherAPIClient weatherApiClient;
    private final LocationService locationService;

    public final static String LOCATION_UPDATE_MESSAGE = "Локация обновлена!\n" +
            "Нажимай /getWeather, чтобы узнать текущую погоду.\n" +
            "Или нажимай /getLocation, чтобы узнать текущее точное местоположение." +
            "Также можешь ввести наименование твоего города (на англ. языке), чтобы обновить локацию. :)";

    private final static String GET_CURRENT_LOCATION_MESSAGE = "Точное текущее местоположение:\n";

    private final static String NOT_FOUND_LOCATION_MESSAGE = "Локация не найдена! Добавь ее в закрепе геолокации в чате";

    private final static String NOT_FOUND_CITY_MESSAGE = "Такой город не найден!";

    private LocationUpdateCommand(UserService userService,
                                  TelegramBot telegramBotSender,
                                  WeatherAPIClient weatherApiClient,
                                  LocationService locationService) {
        this.telegramBotSender = telegramBotSender;
        this.userService = userService;
        this.weatherApiClient = weatherApiClient;
        this.locationService = locationService;
    }

    /**
     * Метод добавления новой локации пользователя,
     * если локация у такого пользователя существует,
     * то в БД происходит обновление
     *
     * @param message
     */
    @Override
    public void execute(Message message) {
        var longitude = message.location().longitude();
        var latitude = message.location().latitude();
        var userId = message.from().id();
        var city = getCity(message.location());

        UserEntity user = getUser(message);

        if (Objects.isNull(user.getLocation())) {
            LocationEntity location = new LocationEntity();

            location.setLongitude(longitude);
            location.setLatitude(latitude);
            location.setCity(city);
            location.setUser(user);

            locationService.saveLocation(location);
        } else {
            locationService.findByUserIdAndUserLocationId(userId, user.getLocation().getId()).orElseGet(() -> {
                LocationEntity updatedLocation = new LocationEntity();

                updatedLocation.setId(user.getLocation().getId());
                updatedLocation.setLongitude(longitude);
                updatedLocation.setLatitude(latitude);
                updatedLocation.setCity(city);
                updatedLocation.setUser(user);

                locationService.updateLocation(updatedLocation, updatedLocation.getId());
                return updatedLocation;
            });
        }

        telegramBotSender.execute(new SendMessage(userId, LOCATION_UPDATE_MESSAGE));
    }


    private String getCity(Location location) {
        return weatherApiClient.getCurrentWeather(location.latitude(),
                location.longitude()).getCity();
    }

    private UserEntity getUser(Message message) {
        return userService.findByUserId(message.from().id()).orElseThrow(
                () -> new UserNotFoundException("User with this id not found in DB!"));
    }

    /**
     * Метод предоставления текущей локации из БД
     *
     * @param message
     */
    public void getCurrentLocation(Message message) {
        UserEntity user = getUser(message);

        if (Objects.isNull(user.getLocation())) {
            telegramBotSender.execute(new SendMessage(message.from().id(), NOT_FOUND_LOCATION_MESSAGE));
            return;
        }

        OpenWeatherDTO openWeatherDTO = weatherApiClient.getCurrentWeather(user.getLocation().getLatitude(),
                user.getLocation().getLongitude());

        telegramBotSender.execute(new SendMessage(message.from().id(), GET_CURRENT_LOCATION_MESSAGE +
                openWeatherDTO.getCoordinate() + openWeatherDTO + openWeatherDTO.getCountry()));
    }

    /**
     * Метод предоставления текущей локации из БД согласно введенному наименованию города
     *
     * @param message
     */
    public void getCurrentLocationFromCity(Message message) {
        String city = message.text();
        OpenWeatherDTO openWeatherDTO = weatherApiClient.getCurrentLocationFromCity(city);

        var cityDTO = openWeatherDTO.getCity();
        var longitudeDTO = openWeatherDTO.getCoordinate().getLon();
        var latitudeDTO = openWeatherDTO.getCoordinate().getLat();

        if (!message.text().equalsIgnoreCase(cityDTO)) {
            telegramBotSender.execute(new SendMessage(message.from().id(), NOT_FOUND_CITY_MESSAGE));
            return;
        }

        UserEntity user = getUser(message);

        if (Objects.isNull(user.getLocation())) {
            LocationEntity location = new LocationEntity();

            location.setLongitude(longitudeDTO);
            location.setLatitude(latitudeDTO);
            location.setCity(cityDTO);
            location.setUser(user);

            locationService.saveLocation(location);
        } else {
            locationService.findByUserIdAndUserLocationId(message.from().id(), user.getLocation().getId()).orElseGet(() -> {
                LocationEntity updatedLocation = new LocationEntity();

                updatedLocation.setId(user.getLocation().getId());
                updatedLocation.setLongitude(longitudeDTO);
                updatedLocation.setLatitude(latitudeDTO);
                updatedLocation.setCity(cityDTO);
                updatedLocation.setUser(user);

                locationService.updateLocation(updatedLocation, updatedLocation.getId());

                return updatedLocation;
            });
        }

        telegramBotSender.execute(new SendMessage(message.from().id(), LOCATION_UPDATE_MESSAGE));
    }
}
