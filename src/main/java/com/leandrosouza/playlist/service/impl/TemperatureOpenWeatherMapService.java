package com.leandrosouza.playlist.service.impl;

import com.leandrosouza.playlist.exceptions.NotFoundException;
import com.leandrosouza.playlist.service.TemperatureService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Qualifier("OpenWeatherMap")
@Slf4j
public class TemperatureOpenWeatherMapService implements TemperatureService {

    private static final String API_KEY = "06819ddf913c9af60a900d2b3d11c056"; // Substitua pela sua chave
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public CompletableFuture<Integer> getTemperatureByCity(String city) {
        log.info("[TemperatureOpenWeatherMapService > getTemperatureByCity] city: {}", city);
        return CompletableFuture.supplyAsync(() -> fetchTemperature(city));
    }

    @Retry(name = "openWeatherMapRetry")
    public Integer fetchTemperature(String city) {
        log.info("[TemperatureOpenWeatherMapService > fetchTemperature] city: {}", city);
        try {
            String url = String.format(API_URL, city, API_KEY);
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            Integer temperature = Objects.requireNonNull(response).getMain().getTemp();
            redisTemplate.opsForValue().set(city, String.valueOf(temperature), 5, TimeUnit.MINUTES);
            return temperature;

        } catch (Exception e) {
            if (e instanceof HttpClientErrorException
                    && ((HttpClientErrorException)e).getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException("City " + city + " not found");
            }
            String cachedTemperature = redisTemplate.opsForValue().get(city);
            if (cachedTemperature != null) {
                log.info("Returning cached temperature: {} for city: {}", cachedTemperature, city);
                return Integer.parseInt(cachedTemperature);
            }
            log.error("Failed: {}", e.getMessage());
            throw e;
        }
    }
}

@Setter
@Getter
class WeatherResponse {
    private Main main;

    @Setter
    @Getter
    static class Main {
        private Integer temp;

    }
}

