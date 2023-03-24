package ru.home.chernyadieva.springweatherapp.util.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoordinateDTO {

    @JsonProperty(value = "lon")
    private double lon;

    @JsonProperty(value = "lat")
    private double lat;

    @Override
    public String toString() {
        return "\nширота = " + lon +
                "\nдолгота = " + lat;
    }
}
