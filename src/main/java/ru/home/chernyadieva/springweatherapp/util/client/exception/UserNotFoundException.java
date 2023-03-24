package ru.home.chernyadieva.springweatherapp.util.client.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
}
