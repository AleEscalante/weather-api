package org.consiti.prueba.weather.model.input.weather.current;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherModel(@JsonProperty("description") String description) {
}
