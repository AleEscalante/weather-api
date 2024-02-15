package org.consiti.prueba.weather.configuration;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CacheBeans {

    private int DURATION = 6;

    @Bean
    public CacheStore<Record> userCache() {
        log.info("Expiry duration of user cache: " + DURATION + " minutes");
        return new CacheStore<>(DURATION, TimeUnit.MINUTES);
    }
}