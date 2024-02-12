package org.consiti.prueba.weather.model.input.weather.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CityModel(
        @JsonProperty("name") String name,
        @JsonProperty("country") String country,
        @JsonProperty("population") int population,
        @JsonProperty("timezone") int timezone,
        @JsonProperty("sunrise") long sunrise,
        @JsonProperty("sunset") long sunset
) {
}
