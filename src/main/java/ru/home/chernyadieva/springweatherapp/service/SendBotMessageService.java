package ru.home.chernyadieva.springweatherapp.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Service;
import ru.home.chernyadieva.springweatherapp.service.command.*;

import java.util.List;
import java.util.Objects;

@Service
public class SendBotMessageService implements UpdatesListener {
    private final StartCommand startCommand;
    private final LocationUpdateCommand locationUpdateCommand;
    private final WeatherCommand weatherCommand;
    private final TelegramBot telegramBotSender;

    public SendBotMessageService(StartCommand startCommand,
                                 LocationUpdateCommand locationUpdateCommand,
                                 WeatherCommand weatherCommand, TelegramBot telegramBotSender) {
        this.startCommand = startCommand;
        this.locationUpdateCommand = locationUpdateCommand;
        this.weatherCommand = weatherCommand;
        this.telegramBotSender = telegramBotSender;
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
            String msg = message.text();

            if (Objects.nonNull(msg) && !msg.isEmpty()) {

                if (msg.equalsIgnoreCase(CommandName.START.getCommandName())) {
                    startCommand.execute(message);
                    continue;
                }

                if (msg.equalsIgnoreCase(CommandName.WEATHER.getCommandName())) {
                    weatherCommand.execute(message);
                    continue;
                }

                if (msg.equalsIgnoreCase(CommandName.GET_LOCATION.getCommandName())) {
                    locationUpdateCommand.getCurrentLocation(message);
                } else {
                    locationUpdateCommand.getCurrentLocationFromCity(message);
                }

            } else if (Objects.nonNull(message.location())) {
                locationUpdateCommand.execute(message);
            }
        }
        return CONFIRMED_UPDATES_ALL;
    }
}