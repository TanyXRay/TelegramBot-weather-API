package ru.home.chernyadieva.springweatherapp.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.home.chernyadieva.springweatherapp.service.command.CommandName;
import ru.home.chernyadieva.springweatherapp.service.command.LocationUpdateCommand;
import ru.home.chernyadieva.springweatherapp.service.command.StartCommand;
import ru.home.chernyadieva.springweatherapp.util.client.WeatherApiClient;

import java.util.List;

@Service
public class SendBotMessageService implements UpdatesListener {
    private final UserService userService;
    private final LocationService locationService;
    private final TelegramBot telegramBotSender;
    private final WeatherApiClient weatherApiClient;
    private final StartCommand startCommand;
    private final LocationUpdateCommand locationUpdateCommand;

    public SendBotMessageService(UserService userService,
                                 LocationService locationService,
                                 @Qualifier(value = "telegramBotSender") TelegramBot telegramBotSender, WeatherApiClient weatherApiClient, StartCommand startCommand, LocationUpdateCommand locationUpdateCommand) {
        this.userService = userService;
        this.locationService = locationService;
        this.telegramBotSender = telegramBotSender;
        this.weatherApiClient = weatherApiClient;
        this.startCommand = startCommand;
        this.locationUpdateCommand = locationUpdateCommand;
    }

    /**
     * Метод приема и обработки сообщений
     *
     * @param updates available updates
     * @return
     */
    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            Message message = update.message();
            if (message.text().isEmpty()){
                return CONFIRMED_UPDATES_ALL;
            }
            if (message.text().equalsIgnoreCase(CommandName.START.getCommandName())){
                startCommand.execute(message);
            }
            locationUpdateCommand.execute(message,message.location());


        }
        return CONFIRMED_UPDATES_ALL;
    }
}