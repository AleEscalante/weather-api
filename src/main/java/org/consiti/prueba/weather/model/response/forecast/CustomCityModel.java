package org.consiti.prueba.weather.model.response.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomCityModel(
        String name,
        String country,
        int population,
        int timezone,
        String sunrise,
        String sunset
) {
}
