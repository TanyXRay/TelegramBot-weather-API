package ru.home.chernyadieva.springweatherapp.util.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OpenWeatherDTO {
    @JsonProperty(value = "coord")
    public CoordinateDTO coordinate;

    @JsonProperty(value = "main")
    public TemperatureDTO main;

    @JsonProperty(value = "sys")
    public CountryDTO country;

    @JsonProperty(value = "name")
    public String city;

    @Override
    public String toString() {
        return "\n город = " + city;
    }
}
