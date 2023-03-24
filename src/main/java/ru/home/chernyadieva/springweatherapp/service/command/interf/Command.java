package ru.home.chernyadieva.springweatherapp.service.command.interf;

import com.pengrad.telegrambot.model.Message;

/**
 * Интерфейс, который реализуют все классы команды
 */
public interface Command {

    void execute(Message message);
}
