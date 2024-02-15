package org.consiti.prueba.weather.service;

import lombok.extern.slf4j.Slf4j;
import org.consiti.prueba.weather.model.input.weather.current.WeatherInfoModel;
import org.consiti.prueba.weather.model.input.weather.forecast.ForecastModel;
import org.consiti.prueba.weather.model.input.weather.pollution.AirPollutionModel;
import org.consiti.prueba.weather.model.response.forecast.CustomCityModel;
import org.consiti.prueba.weather.model.response.forecast.CustomForecastResponseModel;
import org.consiti.prueba.weather.model.response.pollution.CustomPollutionInfoResponseModel;
import org.consiti.prueba.weather.model.response.weather.CustomWeatherResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class WeatherInfoService {

    @Autowired
    private RestTemplate restTemplate;

    // Current weather
    public WeatherInfoModel getWeatherCurrent(String baseUrl, String lat, String lon, String key) {
        String url = baseUrl + lat + "&lon=" + lon + "&appid=" + key + "&units=metric";
        ResponseEntity<WeatherInfoModel> response = this.restTemplate.getForEntity(url, WeatherInfoModel.class);
        return response.getBody();
    }

    public CustomWeatherResponseModel transformToCustomWeatherResponse(WeatherInfoModel weatherInfoResponse) {
        return new CustomWeatherResponseModel(weatherInfoResponse.weather().get(0).description(),
                weatherInfoResponse.main().temp(),
                weatherInfoResponse.main().feelsLike(),
                weatherInfoResponse.main().pressure(),
                weatherInfoResponse.main().humidity(),
                weatherInfoResponse.windModel());
    }

    // Forescast
    public ForecastModel getForecast(String baseUrl, String lat, String lon, String key) {
        String url = baseUrl + lat + "&lon=" + lon + "&appid=" + key + "&units=metric";
        ResponseEntity<ForecastModel> response = this.restTemplate.getForEntity(url, ForecastModel.class);
        return response.getBody();
    }

    public int getConvertedTimezone(int timezone) {
        return timezone / 3600;
    }

    public String getEpochToString(long epoch) {
        Date date = new Date(epoch * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public CustomForecastResponseModel transformToCustomForecastResponse(ForecastModel forecastResponse) {
        CustomCityModel customCityModel = new CustomCityModel(
                forecastResponse.cityModel().name(),
                forecastResponse.cityModel().country(),
                forecastResponse.cityModel().population(),
                this.getConvertedTimezone(forecastResponse.cityModel().timezone()),
                this.getEpochToString(forecastResponse.cityModel().sunrise()),
                this.getEpochToString(forecastResponse.cityModel().sunset())
        );
        return new CustomForecastResponseModel(
                forecastResponse.count(),
                forecastResponse.list(),
                customCityModel
        );
    }

    // Air pollution
    public AirPollutionModel getAirPollution(String baseUrl, String lat, String lon, String key) {
        String url = baseUrl + lat + "&lon=" + lon + "&appid=" + key + "&units=metric";
        ResponseEntity<AirPollutionModel> response = this.restTemplate.getForEntity(url, AirPollutionModel.class);
        return response.getBody();
    }

    public CustomPollutionInfoResponseModel transformToCustomPollutionResponse(AirPollutionModel airPollutionResponse) {
        return new CustomPollutionInfoResponseModel(
                airPollutionResponse.list().get(0).main().aqi(),
                airPollutionResponse.list().get(0).components()
        );
    }

}
