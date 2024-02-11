package org.consiti.prueba.weather.controller;

import org.consiti.prueba.weather.dto.Mensaje;
import org.consiti.prueba.weather.model.input.LocationModel;
import org.consiti.prueba.weather.model.input.WeatherInfoModel;
import org.consiti.prueba.weather.model.response.CustomWeatherResponse;
import org.consiti.prueba.weather.service.CurrentWeatherService;
import org.consiti.prueba.weather.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/current-weather"})
@CrossOrigin(origins = {"*"})
@CacheConfig(cacheNames={"weatherCache"})
public class CurrentWeatherController {

    @Autowired
    private LocationService locationService;
    @Autowired
    private CurrentWeatherService currentWeatherService;
    @Value("${weather.api-key}")
    private String apiKey;

    @GetMapping("/{city}")
    @Cacheable(key = "'weather-'+#city",unless = "#result == null")
    public ResponseEntity<?> getWeather(@PathVariable("city") String city) {
        LocationModel location = locationService.getLocation(city);
        if (location == null) {
            return new ResponseEntity<>(new Mensaje("La ciudad solicitada no existe"), HttpStatus.NOT_FOUND);
        } else {
            WeatherInfoModel weatherInfo = this.currentWeatherService.getWeatherCurrent(location.lat(), location.lon(), this.apiKey);
            CustomWeatherResponse customResponse = this.currentWeatherService.transformToCustomResponse(weatherInfo);
            return ResponseEntity.ok(customResponse);
        }
    }
}
