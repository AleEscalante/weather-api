package org.consiti.prueba.weather.model.response.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.consiti.prueba.weather.model.input.weather.forecast.DailyInfoModel;

import java.util.List;

public record CustomForecastResponseModel(
        String count,
        List<DailyInfoModel> list,
        @JsonProperty("city-info") CustomCityModel cityModel
) {
}
