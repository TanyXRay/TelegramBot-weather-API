package ru.home.chernyadieva.springweatherapp.util.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TemperatureDTO {

    @JsonProperty(value = "temp")
    private double temp;

    @JsonProperty(value = "feels_like")
    private double feels_like;

    @JsonProperty(value = "pressure")
    private int pressure;

    @JsonProperty(value = "humidity")
    private double humidity;

    @Override
    public String toString() {
        return "\nтемпература = " + Math.round(temp) +
                "\nощущается как = " + Math.round(feels_like) +
                "\nатм. давление = " + pressure +
                "\nвлажность = " + humidity;
    }
}
