package org.consiti.prueba.weather.model.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MainModel(
        @JsonProperty("temp") double temp,
        @JsonProperty("feels_like") double feelsLike,
        @JsonProperty("pressure") int pressure,
        @JsonProperty("humidity") int humidity) {
}
