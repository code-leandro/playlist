package com.leandrosouza.playlist.service.impl;

import com.leandrosouza.playlist.service.TemperatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
@Qualifier("TemperatureSimulateFail")
@Slf4j
public class TemperatureSimulateFailServiceImpl implements TemperatureService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Async
    @Override
    public CompletableFuture<Integer> getTemperatureByCity(String city) {
        log.info("[TemperatureSimulateFailServiceImpl > getTemperatureByCity] city: {}", city);
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Attempting to get temperature for city: {}", city);
                throw new RuntimeException("Simulated service failure");

            } catch (Exception e) {
                log.error("Failed to retrieve temperature for city: {}. Error: {}", city, e.getMessage());

                String cachedTemperature = redisTemplate.opsForValue().get(city);
                if (cachedTemperature != null) {
                    log.info("Returning cached temperature: {} for city: {}", cachedTemperature, city);
                    return Integer.parseInt(cachedTemperature);
                } else {
                    log.error("No cached temperature found for city: {}", city);
                    throw new RuntimeException("Temperature data unavailable for city: " + city);
                }
            }
        });
    }
}
