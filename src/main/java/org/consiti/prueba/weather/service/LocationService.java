package org.consiti.prueba.weather.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.consiti.prueba.weather.model.input.LocationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LocationService {
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Value("${weather.location-api-url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    public LocationModel getLocation(String city) {
        String url = this.apiUrl + city + "&format=json";
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
