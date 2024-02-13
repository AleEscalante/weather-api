package org.consiti.prueba.weather.controller;

import org.consiti.prueba.weather.dto.Message;
import org.consiti.prueba.weather.model.input.location.LocationModel;
import org.consiti.prueba.weather.model.input.weather.current.WeatherInfoModel;
import org.consiti.prueba.weather.model.input.weather.forecast.ForecastModel;
import org.consiti.prueba.weather.model.input.weather.pollution.AirPollutionModel;
import org.consiti.prueba.weather.model.response.forecast.CustomForecastModel;
import org.consiti.prueba.weather.model.response.pollution.CustomPollutionInfoModel;
import org.consiti.prueba.weather.model.response.weather.CustomWeatherResponse;
import org.consiti.prueba.weather.service.weather.WeatherInfoService;
import org.consiti.prueba.weather.service.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/info"})
@CrossOrigin(origins = {"*"})
@CacheConfig(cacheNames = {"weatherCache"})
public class WeatherController {

    @Autowired
    private LocationService locationService;
    @Autowired
    private WeatherInfoService weatherInfoService;
    @Value("${weather.api-key}")
    private String apiKey;

    @GetMapping("/current-weather/{city}")
    @Cacheable(key = "'weather-'+#city", unless = "#result == null")
    public ResponseEntity<?> getWeather(@PathVariable("city") String city) {
        LocationModel location = locationService.getLocation(city, apiKey);
        if (location == null) {
            return new ResponseEntity<>(new Message("La ciudad solicitada no existe"), HttpStatus.NOT_FOUND);
        } else {
            WeatherInfoModel weatherInfoResponse = this.weatherInfoService.getWeatherCurrent(location.lat(), location.lon(), this.apiKey);
            CustomWeatherResponse customResponse = this.weatherInfoService.transformToCustomWeatherResponse(weatherInfoResponse);
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
    }

    @GetMapping("/forecast/{city}")
    @Cacheable(key = "'forecast-'+#city", unless = "#result == null")
    public ResponseEntity<?> getForecast(@PathVariable("city") String city) {
        LocationModel location = locationService.getLocation(city, apiKey);
        if (location == null) {
            return new ResponseEntity<>(new Message("La ciudad solicitada no existe"), HttpStatus.NOT_FOUND);
        } else {
            ForecastModel forecastResponse = this.weatherInfoService.getForecast(location.lat(), location.lon(), this.apiKey);
            CustomForecastModel customResponse = this.weatherInfoService.transformToCustomForecastResponse(forecastResponse);
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
    }

    @GetMapping("/air-pollution/{city}")
    @Cacheable(key = "'pollution-'+#city", unless = "#result == null")
    public ResponseEntity<?> getAirPollution(@PathVariable("city") String city) {
        LocationModel location = locationService.getLocation(city, apiKey);
        if (location == null) {
            return new ResponseEntity<>(new Message("La ciudad solicitada no existe"), HttpStatus.NOT_FOUND);
        } else {
            AirPollutionModel airPollutionResponse = this.weatherInfoService.getAirPollution(location.lat(), location.lon(), this.apiKey);
            CustomPollutionInfoModel customResponse = this.weatherInfoService.transformToCustomPollutionResponse(airPollutionResponse);
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
    }

}
