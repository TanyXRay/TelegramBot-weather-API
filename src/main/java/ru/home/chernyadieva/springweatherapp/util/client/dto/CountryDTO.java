package ru.home.chernyadieva.springweatherapp.util.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountryDTO {

    @JsonProperty(value = "country")
    private String country;

    @Override
    public String toString() {
        return "\nстрана = " + country;
    }
}
