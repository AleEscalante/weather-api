package org.consiti.prueba.weather.model.input.weather.pollution;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ConcentrationsModel(
        @JsonProperty("co") double co,
        @JsonProperty("no") double no,
        @JsonProperty("no2") double no2,
        @JsonProperty("o3") double o3,
        @JsonProperty("so2") double so2,
        @JsonProperty("pm2_5") double fineParticles,
        @JsonProperty("pm10") double coarseParticulate,
        @JsonProperty("nh3") double nh3
) {
}
