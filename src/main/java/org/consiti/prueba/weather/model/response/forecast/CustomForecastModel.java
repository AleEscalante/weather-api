package org.consiti.prueba.weather.model.response.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.consiti.prueba.weather.model.input.weather.forecast.CityModel;
import org.consiti.prueba.weather.model.input.weather.forecast.DailyInfoModel;

import java.util.List;

public record CustomForecastModel(
        String count,
        List<DailyInfoModel> list,
        @JsonProperty("city-info") CustomCityModel cityModel
) {
}
