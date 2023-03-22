package ru.home.chernyadieva.springweatherapp.service.command;

public enum CommandName {

    START("/start"),
    STOP("/getWeather"),
    NO("nocommand");


    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}