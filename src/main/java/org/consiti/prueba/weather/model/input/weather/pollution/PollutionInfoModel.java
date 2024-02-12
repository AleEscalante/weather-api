package org.consiti.prueba.weather.model.input.weather.pollution;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PollutionInfoModel(
        @JsonProperty("main") MainPollutionModel main,
        @JsonProperty("components") ConcentrationsModel components
) {
}
