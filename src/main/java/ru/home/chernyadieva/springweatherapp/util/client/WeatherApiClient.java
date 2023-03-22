package ru.home.chernyadieva.springweatherapp.util.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.home.chernyadieva.springweatherapp.util.client.dto.OpenWeatherDTO;
import ru.home.chernyadieva.springweatherapp.util.client.exception.RestTemplateResponseErrorHandler;

@Component
public class WeatherApiClient {
    private final RestTemplate restTemplate;

    @Value(value = "${app.open-weather-map-token}")
    private String weatherToken;

    public WeatherApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    }

    public OpenWeatherDTO getCurrentWeather(double latitude, double longitude, String units) {
        UriComponents uriBuilder = UriComponentsBuilder.newInstance()
                .scheme("https").host("api.openweathermap.org").path("data/2.5/weather")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("units", units)
                .queryParam("appid", weatherToken)
                .build(true);

        return restTemplate.getForObject(uriBuilder.toUriString(), OpenWeatherDTO.class);
    }
}
