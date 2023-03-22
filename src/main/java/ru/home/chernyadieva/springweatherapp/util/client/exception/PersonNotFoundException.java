package ru.home.chernyadieva.springweatherapp.util.client.exception;

public class PersonNotFoundException extends RuntimeException{

    public PersonNotFoundException (String message) {
        super(message);
    }
}
