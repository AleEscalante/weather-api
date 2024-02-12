package org.consiti.prueba.weather.model.input.weather.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MainDailyModel(
        @JsonProperty("temp") double temp,
        @JsonProperty("feels_like") double feelsLike,
        @JsonProperty("temp_min") double temp_min,
        @JsonProperty("temp_max") double temp_max,
        @JsonProperty("humidity") double humidity
) {
}
