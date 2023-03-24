package ru.home.chernyadieva.springweatherapp.util.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.home.chernyadieva.springweatherapp.util.client.dto.OpenWeatherDTO;
import ru.home.chernyadieva.springweatherapp.util.client.exception.RestTemplateResponseErrorHandler;

@Component
public class WeatherAPIClient {
    private final RestTemplate restTemplate;

    @Value(value = "${app.open-weather-map-token}")
    private String weatherToken;

    @Value(value = "${app.open-weather-map-units}")
    private String units;

    @Value(value = "${app.open-weather-map-lang}")
    private String lang;

    public WeatherAPIClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    }

    /**
     * Метод запроса на API текущей погоды
     * @param latitude
     * @param longitude
     * @return
     */
    public OpenWeatherDTO getCurrentWeather(double latitude, double longitude) {
        UriComponents uriBuilder = UriComponentsBuilder.newInstance()
                .scheme("https").host("api.openweathermap.org").path("data/2.5/weather")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("units", units)
                .queryParam("lang", lang)
                .queryParam("appid", weatherToken)
                .build(true);

        return restTemplate.getForObject(uriBuilder.toUriString(), OpenWeatherDTO.class);
    }

    /**
     * Метод запроса на API текущей локации исходя из наименования города
     * @param city
     * @return
     */
    public OpenWeatherDTO getCurrentLocationFromCity(String city) {
        UriComponents uriBuilder = UriComponentsBuilder.newInstance()
                .scheme("https").host("api.openweathermap.org").path("data/2.5/weather")
                .queryParam("q", city)
                .queryParam("units", units)
                .queryParam("appid", weatherToken)
                .build(true);

        return restTemplate.getForObject(uriBuilder.toUriString(), OpenWeatherDTO.class);
    }
}
