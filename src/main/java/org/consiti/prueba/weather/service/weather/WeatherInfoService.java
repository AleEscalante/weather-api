package org.consiti.prueba.weather.service.weather;

import org.consiti.prueba.weather.model.input.weather.current.WeatherInfoModel;
import org.consiti.prueba.weather.model.input.weather.forecast.ForecastModel;
import org.consiti.prueba.weather.model.input.weather.pollution.AirPollutionModel;
import org.consiti.prueba.weather.model.response.forecast.CustomCityModel;
import org.consiti.prueba.weather.model.response.forecast.CustomForecastModel;
import org.consiti.prueba.weather.model.response.pollution.CustomPollutionInfoModel;
import org.consiti.prueba.weather.model.response.weather.CustomWeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WeatherInfoService {

    @Value("${weather.weather-api-url}")
    private String apiCurrentWeatherUrl;
    @Value("${weather.air-pollution-api-url}")
    private String apiAirPollutionUrl;
    @Value("${weather.forecast-api-url}")
    private String apiForecastUrl;

    @Autowired
    private RestTemplate restTemplate;

    // Current weather
    public WeatherInfoModel getWeatherCurrent(String lat, String lon, String key) {
        String url = this.apiCurrentWeatherUrl + lat + "&lon=" + lon + "&appid=" + key + "&units=metric";
        ResponseEntity<WeatherInfoModel> response = this.restTemplate.getForEntity(url, WeatherInfoModel.class);
        return response.getBody();
    }

    public CustomWeatherResponse transformToCustomWeatherResponse(WeatherInfoModel weatherInfoResponse) {
        return new CustomWeatherResponse(weatherInfoResponse.weather().get(0).description(),
                weatherInfoResponse.main().temp(),
                weatherInfoResponse.main().feelsLike(),
                weatherInfoResponse.main().pressure(),
                weatherInfoResponse.main().humidity(),
                weatherInfoResponse.windModel());
    }

    // Forescast
    public ForecastModel getForecast(String lat, String lon, String key) {
        String url = this.apiForecastUrl + lat + "&lon=" + lon + "&appid=" + key + "&units=metric";
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

    public CustomForecastModel transformToCustomForecastResponse(ForecastModel forecastResponse) {
        CustomCityModel customCityModel = new CustomCityModel(
                forecastResponse.cityModel().name(),
                forecastResponse.cityModel().country(),
                forecastResponse.cityModel().population(),
                this.getConvertedTimezone(forecastResponse.cityModel().timezone()),
                this.getEpochToString(forecastResponse.cityModel().sunrise()),
                this.getEpochToString(forecastResponse.cityModel().sunset())
        );
        return new CustomForecastModel(
                forecastResponse.count(),
                forecastResponse.list(),
                customCityModel
        );
    }

    // Air pollution
    public AirPollutionModel getAirPollution(String lat, String lon, String key) {
        String url = this.apiAirPollutionUrl + lat + "&lon=" + lon + "&appid=" + key + "&units=metric";
        ResponseEntity<AirPollutionModel> response = this.restTemplate.getForEntity(url, AirPollutionModel.class);
        return response.getBody();
    }

    public CustomPollutionInfoModel transformToCustomPollutionResponse(AirPollutionModel airPollutionResponse) {
        return new CustomPollutionInfoModel(
                airPollutionResponse.list().get(0).main().aqi(),
                airPollutionResponse.list().get(0).components()
        );
    }
}
