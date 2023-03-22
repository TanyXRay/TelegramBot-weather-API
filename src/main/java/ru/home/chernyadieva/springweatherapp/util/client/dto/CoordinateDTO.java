package ru.home.chernyadieva.springweatherapp.util.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoordinateDTO {
    public double lon;
    public double lat;

    @Override
    public String toString() {
        return "\n широта = " + lon +
                "\n долгота = " + lat;
    }
}
