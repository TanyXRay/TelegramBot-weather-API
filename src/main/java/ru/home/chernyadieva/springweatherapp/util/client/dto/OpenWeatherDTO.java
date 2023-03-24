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
    private CoordinateDTO coordinate;

    @JsonProperty(value = "main")
    private TemperatureDTO main;

    @JsonProperty(value = "sys")
    private CountryDTO country;

    @JsonProperty(value = "name")
    private String city;

    @Override
    public String toString() {
        return "\nгород = " + city;
    }
}
