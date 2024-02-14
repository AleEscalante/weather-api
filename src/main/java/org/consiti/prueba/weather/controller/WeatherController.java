package org.consiti.prueba.weather.controller;

import io.github.bucket4j.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.consiti.prueba.weather.configuration.CacheStore;
import org.consiti.prueba.weather.dto.Message;
import org.consiti.prueba.weather.model.input.location.LocationModel;
import org.consiti.prueba.weather.model.input.weather.current.WeatherInfoModel;
import org.consiti.prueba.weather.model.input.weather.forecast.ForecastModel;
import org.consiti.prueba.weather.model.input.weather.pollution.AirPollutionModel;
import org.consiti.prueba.weather.model.response.forecast.CustomForecastModel;
import org.consiti.prueba.weather.model.response.pollution.CustomPollutionInfoModel;
import org.consiti.prueba.weather.model.response.weather.CustomWeatherResponse;
import org.consiti.prueba.weather.security.enums.QueryType;
import org.consiti.prueba.weather.security.service.UserService;
import org.consiti.prueba.weather.service.ConsultationService;
import org.consiti.prueba.weather.service.WeatherInfoService;
import org.consiti.prueba.weather.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/info"})
@CrossOrigin(origins = {"*"})
@SecurityRequirement(name = "Authorization")
@Slf4j
public class WeatherController {

    @Autowired
    private LocationService locationService;
    @Autowired
    private WeatherInfoService weatherInfoService;
    @Autowired
    private ConsultationService consultationService;
    @Autowired
    private UserService userService;
    @Value("${weather.api-key}")
    private String apiKey;
    @Autowired
    private CacheStore<Record> userCacheStore;

    @GetMapping("/current-weather/{city}")
    public ResponseEntity<?> getWeather(@PathVariable("city") String city, HttpServletRequest request) {
        LocationModel location = locationService.getLocation(city, apiKey);
        if (location == null) {
            return new ResponseEntity<>(new Message("La ciudad solicitada no existe"), HttpStatus.NOT_FOUND);
        } else {
            Record userQueryCache = weatherInfoService.loadCache(city, apiKey, weatherInfoService, request, userService,
                    userCacheStore, QueryType.CURRENT_WEATHER);
            String keyCache = weatherInfoService.getKeyCache(QueryType.CURRENT_WEATHER,
                    city, apiKey, weatherInfoService.getUser(weatherInfoService.getTokenFromRequest(request), userService).getUsername());
            if (userQueryCache != null) {
                log.info("Query found in cache with key: " + keyCache);
                consultationService.save(weatherInfoService, request, QueryType.CURRENT_WEATHER, userService, location, apiKey, userQueryCache);
                return new ResponseEntity<>(userQueryCache, HttpStatus.OK);
            }
            log.info("Query not found in cache");
            WeatherInfoModel weatherInfoResponse = this.weatherInfoService.getWeatherCurrent(location.lat(), location.lon(), this.apiKey);
            CustomWeatherResponse customResponse = this.weatherInfoService.transformToCustomWeatherResponse(weatherInfoResponse);
            consultationService.save(weatherInfoService, request, QueryType.CURRENT_WEATHER, userService, location, apiKey, customResponse);
            userCacheStore.add(keyCache, customResponse);
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
    }

    @GetMapping("/forecast/{city}")
    public ResponseEntity<?> getForecast(@PathVariable("city") String city, HttpServletRequest request) {
        LocationModel location = locationService.getLocation(city, apiKey);
        if (location == null) {
            return new ResponseEntity<>(new Message("La ciudad solicitada no existe"), HttpStatus.NOT_FOUND);
        } else {
            Record userQueryCache = weatherInfoService.loadCache(city, apiKey, weatherInfoService, request, userService,
                    userCacheStore, QueryType.FORECAST);
            String keyCache = weatherInfoService.getKeyCache(QueryType.FORECAST,
                    city, apiKey, weatherInfoService.getUser(weatherInfoService.getTokenFromRequest(request), userService).getUsername());
            if (userQueryCache != null) {
                log.info("Query found in cache with key: " + keyCache);
                consultationService.save(weatherInfoService, request, QueryType.FORECAST, userService, location, apiKey, userQueryCache);
                return new ResponseEntity<>(userQueryCache, HttpStatus.OK);
            }
            log.info("Query not found in cache");
            ForecastModel forecastResponse = this.weatherInfoService.getForecast(location.lat(), location.lon(), this.apiKey);
            CustomForecastModel customResponse = this.weatherInfoService.transformToCustomForecastResponse(forecastResponse);
            consultationService.save(weatherInfoService, request, QueryType.FORECAST, userService, location, apiKey, customResponse);
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
    }

    @GetMapping("/air-pollution/{city}")
    public ResponseEntity<?> getAirPollution(@PathVariable("city") String city, HttpServletRequest request) {
        LocationModel location = locationService.getLocation(city, apiKey);
        if (location == null) {
            return new ResponseEntity<>(new Message("La ciudad solicitada no existe"), HttpStatus.NOT_FOUND);
        } else {
            Record userQueryCache = weatherInfoService.loadCache(city, apiKey, weatherInfoService, request, userService,
                    userCacheStore, QueryType.POLLUTION);
            String keyCache = weatherInfoService.getKeyCache(QueryType.POLLUTION,
                    city, apiKey, weatherInfoService.getUser(weatherInfoService.getTokenFromRequest(request), userService).getUsername());
            if (userQueryCache != null) {
                log.info("Query found in cache with key: " + keyCache);
                consultationService.save(weatherInfoService, request, QueryType.POLLUTION, userService, location, apiKey, userQueryCache);
                return new ResponseEntity<>(userQueryCache, HttpStatus.OK);
            }
            log.info("Query not found in cache");
            AirPollutionModel airPollutionResponse = this.weatherInfoService.getAirPollution(location.lat(), location.lon(), this.apiKey);
            CustomPollutionInfoModel customResponse = this.weatherInfoService.transformToCustomPollutionResponse(airPollutionResponse);
            consultationService.save(weatherInfoService, request, QueryType.POLLUTION, userService, location, apiKey, customResponse);
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
    }

}
