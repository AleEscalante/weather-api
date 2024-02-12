package org.consiti.prueba.weather.model.input.weather.pollution;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MainPollutionModel(
        @JsonProperty("aqi") int aqi
) {
}
