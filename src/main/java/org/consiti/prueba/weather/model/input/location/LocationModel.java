package org.consiti.prueba.weather.model.input.location;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LocationModel(
        @JsonProperty("lat") String lat,
        @JsonProperty("lon") String lon) {
}
