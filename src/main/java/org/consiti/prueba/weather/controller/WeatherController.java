package org.consiti.prueba.weather.controller;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.consiti.prueba.weather.configuration.CacheStore;
import org.consiti.prueba.weather.dto.Message;
import org.consiti.prueba.weather.entity.Consultation;
import org.consiti.prueba.weather.model.input.location.LocationModel;
import org.consiti.prueba.weather.model.input.weather.current.WeatherInfoModel;
import org.consiti.prueba.weather.model.input.weather.forecast.ForecastModel;
import org.consiti.prueba.weather.model.input.weather.pollution.AirPollutionModel;
import org.consiti.prueba.weather.model.input.weather.pollution.PollutionInfoModel;
import org.consiti.prueba.weather.model.response.forecast.CustomForecastResponseModel;
import org.consiti.prueba.weather.model.response.pollution.CustomPollutionInfoResponseModel;
import org.consiti.prueba.weather.model.response.weather.CustomWeatherResponseModel;
import org.consiti.prueba.weather.security.dto.JwtDto;
import org.consiti.prueba.weather.security.entity.User;
import org.consiti.prueba.weather.security.enums.QueryType;
import org.consiti.prueba.weather.security.service.UserService;
import org.consiti.prueba.weather.security.util.TokenUtils;
import org.consiti.prueba.weather.service.ConsultationService;
import org.consiti.prueba.weather.service.LocationService;
import org.consiti.prueba.weather.service.WeatherInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

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
    @Value("${weather.weather-api-url}")
    private String apiCurrentWeatherUrl;
    @Value("${weather.air-pollution-api-url}")
    private String apiAirPollutionUrl;
    @Value("${weather.forecast-api-url}")
    private String apiForecastUrl;

    @GetMapping("/current-weather/{city}")
    public ResponseEntity<?> getWeather(@PathVariable("city") String city, HttpServletRequest request) {
        LocationModel location = locationService.getLocation(city, apiKey);
        String keyCache = this.getKeyCache(QueryType.CURRENT_WEATHER, city);
        if (location == null) {
            Message message = new Message("City not found");
            Consultation consultation = new Consultation(
                    this.getUser(this.getTokenFromRequest(request)),
                    new Timestamp(new Date().getTime()),
                    QueryType.CURRENT_WEATHER,
                    this.getUrl(QueryType.CURRENT_WEATHER, null, null),
                    message.toString());
            consultationService.save(consultation);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            Record userQueryCache = this.loadCache(city, userCacheStore, QueryType.CURRENT_WEATHER);
            if (userQueryCache != null) {
                log.info("Query found in cache with key: " + keyCache);
                Consultation consultation = new Consultation(
                        this.getUser(this.getTokenFromRequest(request)),
                        new Timestamp(new Date().getTime()),
                        QueryType.CURRENT_WEATHER,
                        this.getUrl(QueryType.CURRENT_WEATHER, null, null),
                        userQueryCache.toString());
                consultationService.save(consultation);
                return new ResponseEntity<>(userQueryCache, HttpStatus.OK);
            }
            log.info("Query not found in cache");
            WeatherInfoModel weatherInfoResponse = this.weatherInfoService.getWeatherCurrent(this.apiCurrentWeatherUrl,
                    location.lat(), location.lon(), this.apiKey);
            CustomWeatherResponseModel customResponse = this.weatherInfoService.transformToCustomWeatherResponse(weatherInfoResponse);
            Consultation consultation = new Consultation(
                    this.getUser(this.getTokenFromRequest(request)),
                    new Timestamp(new Date().getTime()),
                    QueryType.CURRENT_WEATHER,
                    this.getUrl(QueryType.CURRENT_WEATHER, location.lat(), location.lon()),
                    customResponse.toString());
            consultationService.save(consultation);
            userCacheStore.add(keyCache, customResponse);
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
    }

    @GetMapping("/forecast/{city}")
    public ResponseEntity<?> getForecast(@PathVariable("city") String city, HttpServletRequest request) {
        LocationModel location = locationService.getLocation(city, apiKey);
        String keyCache = this.getKeyCache(QueryType.FORECAST, city);
        if (location == null) {
            Message message = new Message("City not found");
            Consultation consultation = new Consultation(
                    this.getUser(this.getTokenFromRequest(request)),
                    new Timestamp(new Date().getTime()),
                    QueryType.FORECAST,
                    this.getUrl(QueryType.FORECAST, null, null),
                    message.toString());
            consultationService.save(consultation);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            Record userQueryCache = this.loadCache(city, userCacheStore, QueryType.FORECAST);
            if (userQueryCache != null) {
                log.info("Query found in cache with key: " + keyCache);
                Consultation consultation = new Consultation(
                        this.getUser(this.getTokenFromRequest(request)),
                        new Timestamp(new Date().getTime()),
                        QueryType.FORECAST,
                        this.getUrl(QueryType.FORECAST, null, null),
                        userQueryCache.toString());
                consultationService.save(consultation);
                return new ResponseEntity<>(userQueryCache, HttpStatus.OK);
            }
            log.info("Query not found in cache");
            ForecastModel forecastInfoResponse = this.weatherInfoService.getForecast(this.apiForecastUrl,
                    location.lat(), location.lon(), this.apiKey);
            CustomForecastResponseModel customResponse = this.weatherInfoService.transformToCustomForecastResponse(forecastInfoResponse);
            Consultation consultation = new Consultation(
                    this.getUser(this.getTokenFromRequest(request)),
                    new Timestamp(new Date().getTime()),
                    QueryType.FORECAST,
                    this.getUrl(QueryType.FORECAST, location.lat(), location.lon()),
                    customResponse.toString());
            consultationService.save(consultation);
            userCacheStore.add(keyCache, customResponse);
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
    }

    @GetMapping("/air-pollution/{city}")
    public ResponseEntity<?> getAirPollution(@PathVariable("city") String city, HttpServletRequest request) {
        LocationModel location = locationService.getLocation(city, apiKey);
        String keyCache = this.getKeyCache(QueryType.POLLUTION, city);
        if (location == null) {
            Message message = new Message("City not found");
            Consultation consultation = new Consultation(
                    this.getUser(this.getTokenFromRequest(request)),
                    new Timestamp(new Date().getTime()),
                    QueryType.POLLUTION,
                    this.getUrl(QueryType.POLLUTION, null, null),
                    message.toString());
            consultationService.save(consultation);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            Record userQueryCache = this.loadCache(city, userCacheStore, QueryType.POLLUTION);
            if (userQueryCache != null) {
                log.info("Query found in cache with key: " + keyCache);
                Consultation consultation = new Consultation(
                        this.getUser(this.getTokenFromRequest(request)),
                        new Timestamp(new Date().getTime()),
                        QueryType.POLLUTION,
                        this.getUrl(QueryType.POLLUTION, null, null),
                        userQueryCache.toString());
                consultationService.save(consultation);
                return new ResponseEntity<>(userQueryCache, HttpStatus.OK);
            }
            log.info("Query not found in cache");
            AirPollutionModel airPollutionResponse = this.weatherInfoService.getAirPollution(this.apiAirPollutionUrl,
                    location.lat(), location.lon(), this.apiKey);
            CustomPollutionInfoResponseModel customResponse = this.weatherInfoService.transformToCustomPollutionResponse(airPollutionResponse);
            Consultation consultation = new Consultation(
                    this.getUser(this.getTokenFromRequest(request)),
                    new Timestamp(new Date().getTime()),
                    QueryType.POLLUTION,
                    this.getUrl(QueryType.POLLUTION, location.lat(), location.lon()),
                    customResponse.toString());
            consultationService.save(consultation);
            userCacheStore.add(keyCache, customResponse);
            return new ResponseEntity<>(customResponse, HttpStatus.OK);
        }
    }

    public String getKeyCache(QueryType queryType, String city) {
        return queryType + city;
    }

    public Record loadCache(String city, CacheStore<Record> userCacheStore, QueryType queryType) {
        String keyCache = getKeyCache(queryType, city);
        return userCacheStore.get(keyCache);
    }

    public User getUser(String token) {
        try {
            return this.getUserFromToken(new JwtDto(token));
        } catch (ParseException e) {
            log.error("Token invalid");
            return new User();
        }
    }

    public String getUrl(QueryType queryType, String lat, String lon) {
        return switch (queryType) {
            case CURRENT_WEATHER ->
                    this.apiCurrentWeatherUrl + lat + "&lon=" + lon + "&appid=" + this.apiKey + "&units=metric";
            case FORECAST -> this.apiForecastUrl + lat + "&lon=" + lon + "&appid=" + this.apiKey + "&units=metric";
            case POLLUTION -> this.apiAirPollutionUrl + lat + "&lon=" + lon + "&appid=" + this.apiKey + "&units=metric";
        };
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.replace("Bearer ", "");
        }
        return null;
    }

    public User getUserFromToken(JwtDto jwtDto) throws ParseException {
        JWT jwt = JWTParser.parse(jwtDto.getToken());
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        String email = claims.getSubject();
        User user = this.userService.getUserByEmail(email);
        if (user != null) {
            log.info("User: " + user);
            return user;
        }
        return null;
    }

}
