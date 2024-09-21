package com.leandrosouza.playlist.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class TemperatureService {

    private static final String API_KEY = "06819ddf913c9af60a900d2b3d11c056"; // Substitua pela sua chave
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    @Autowired
    private RestTemplate restTemplate;

    public CompletableFuture<Integer> getTemperatureByCity(String city) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = String.format(API_URL, city, API_KEY);
                WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
                assert response != null;
                return response.getMain().getTemp();
            } catch (Exception e) {
                log.warn("Failed to get temperature");
                throw new RuntimeException("Failed to get temperature", e);
            }
        });
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

