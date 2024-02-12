package org.consiti.prueba.weather.model.input.weather.current;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MainModel(
        @JsonProperty("temp") double temp,
        @JsonProperty("feels_like") double feelsLike,
        @JsonProperty("pressure") double pressure,
        @JsonProperty("humidity") double humidity) {
}
