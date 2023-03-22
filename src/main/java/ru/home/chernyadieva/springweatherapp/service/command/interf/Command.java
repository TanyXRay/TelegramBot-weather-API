package ru.home.chernyadieva.springweatherapp.service.command.interf;

import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

public interface Command {

    default void execute(Message message){

    }

   default void execute(Message message, Location location){

   };
}
