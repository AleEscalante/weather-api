package org.consiti.prueba.weather.model.response.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.consiti.prueba.weather.model.input.weather.current.WindModel;

public record CustomWeatherResponseModel(
        @JsonProperty("weather") String description,
        double temperature,
        @JsonProperty("feels_like") double feelsLike,
        double pressure,
        double humidity,
        WindModel wind) {
}
