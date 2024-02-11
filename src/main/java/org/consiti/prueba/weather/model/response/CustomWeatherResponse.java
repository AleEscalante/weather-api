package org.consiti.prueba.weather.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomWeatherResponse(
        @JsonProperty("weather") String description,
        double temperature,
        @JsonProperty("feels_like") double feelsLike,
        int pressure, int humidity,
        Wind wind) {
}
