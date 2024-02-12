package org.consiti.prueba.weather.service.location;

import org.consiti.prueba.weather.model.input.location.LocationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationService {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Value("${weather.location-api-url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    public LocationModel getLocation(String city, String apiKey) {
        String url = this.apiUrl + city + "&limit=1&appid=" + apiKey;
        ResponseEntity<LocationModel[]> response = this.restTemplate.getForEntity(url, LocationModel[].class);
        LocationModel[] locations = response.getBody();
        if (locations != null && locations.length > 0) {
            logger.info(locations[0].toString());
            return locations[0];
        } else {
            return null;
        }
    }
}
