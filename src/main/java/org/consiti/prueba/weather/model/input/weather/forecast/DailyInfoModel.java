package org.consiti.prueba.weather.model.input.weather.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.consiti.prueba.weather.model.input.weather.current.WeatherModel;
import org.consiti.prueba.weather.model.input.weather.current.WindModel;

import java.util.List;

public record DailyInfoModel(
        @JsonProperty("main") MainDailyModel main,
        @JsonProperty("weather") List<WeatherModel> weather,
        @JsonProperty("wind") WindModel wind,
        @JsonProperty("dt_txt") String date
) {
}
