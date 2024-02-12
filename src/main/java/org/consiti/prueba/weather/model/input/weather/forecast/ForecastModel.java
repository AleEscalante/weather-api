package org.consiti.prueba.weather.model.input.weather.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ForecastModel (
        @JsonProperty("cnt") String count,
        @JsonProperty("list") List<DailyInfoModel> list,
        @JsonProperty("city") CityModel cityModel

) {}
