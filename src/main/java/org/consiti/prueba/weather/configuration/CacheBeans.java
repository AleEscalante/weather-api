package org.consiti.prueba.weather.configuration;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CacheBeans {

    @Value("${cache.duration.time}")
    private int duration;

    @Bean
    public CacheStore<Record> employeeCache() {
        log.info("Expiry duration of cache: " + duration + " minutes");
        return new CacheStore<>(duration, TimeUnit.MINUTES);
    }
}