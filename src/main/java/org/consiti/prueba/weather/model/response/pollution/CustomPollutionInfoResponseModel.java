package org.consiti.prueba.weather.model.response.pollution;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.consiti.prueba.weather.model.input.weather.pollution.ConcentrationsModel;

public record CustomPollutionInfoResponseModel(

        @JsonProperty("air-quality-index") int aqi,
        ConcentrationsModel components
) {
}
