package org.consiti.prueba.weather.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.consiti.prueba.weather.model.input.WeatherInfoModel;
import org.consiti.prueba.weather.model.response.CustomWeatherResponse;
import org.consiti.prueba.weather.model.response.Wind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWeatherService {

    @Value("${weather.weather-api-url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    public WeatherInfoModel getWeatherCurrent(String lat, String lon, String key) {
        String url = this.apiUrl + lat + "&lon=" + lon + "&appid=" + key + "&units=metric";
        ResponseEntity<WeatherInfoModel> response = this.restTemplate.getForEntity(url, WeatherInfoModel.class);
        return response.getBody();
    }

    public CustomWeatherResponse transformToCustomResponse(WeatherInfoModel weatherInfo) {
        Wind wind = new Wind(weatherInfo.windModel().speed(), weatherInfo.windModel().direction());
        return new CustomWeatherResponse(weatherInfo.weather().get(0).description(),
                weatherInfo.main().temp(),
                weatherInfo.main().feelsLike(),
                weatherInfo.main().pressure(),
                weatherInfo.main().humidity(),
                wind);
    }
}
