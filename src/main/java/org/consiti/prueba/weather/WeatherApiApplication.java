package org.consiti.prueba.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class WeatherApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeatherApiApplication.class, args);
    }

}
