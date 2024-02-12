package org.consiti.prueba.weather.model.input.weather.current;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherInfoModel(
        @JsonProperty("weather") List<WeatherModel> weather,
        @JsonProperty("main") MainModel main,
        @JsonProperty("wind") WindModel windModel) {
}
