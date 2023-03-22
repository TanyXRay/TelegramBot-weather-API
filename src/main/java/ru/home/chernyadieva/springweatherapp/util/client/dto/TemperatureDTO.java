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
    public double temp;

    @JsonProperty(value = "feels_like")
    public double feels_like;

    @JsonProperty(value = "pressure")
    public int pressure;

    @JsonProperty(value = "humidity")
    public double humidity;

    @Override
    public String toString() {
        return "\n температура = " + Math.round(temp) +
                "\n ощущается как = " + Math.round(feels_like) +
                "\n атм. давление = " + pressure +
                "\n влажность = " + humidity;
    }
}
