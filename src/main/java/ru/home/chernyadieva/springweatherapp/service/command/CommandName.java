package ru.home.chernyadieva.springweatherapp.service.command;

/**
 * Класс - представления команд телеграмм-бота
 */
public enum CommandName {

    START("/start"),
    WEATHER("/getWeather"),
    GET_LOCATION("/getLocation");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}