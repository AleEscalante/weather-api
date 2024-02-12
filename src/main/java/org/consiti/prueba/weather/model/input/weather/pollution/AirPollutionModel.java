package org.consiti.prueba.weather.model.input.weather.pollution;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AirPollutionModel(
        @JsonProperty("list") List<PollutionInfoModel> list

) {
}
