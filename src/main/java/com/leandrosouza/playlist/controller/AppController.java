package com.leandrosouza.playlist.controller;

import com.leandrosouza.playlist.api.PlaylistResponse;
import com.leandrosouza.playlist.service.PlaylistService;
import com.leandrosouza.playlist.service.TemperatureService;
import com.leandrosouza.playlist.service.impl.TemperatureOpenWeatherMapService;
import com.leandrosouza.playlist.service.impl.TemperatureSimulateFailServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@EnableAsync
@Slf4j
public class AppController {

    private TemperatureService temperatureService;

    @Autowired
    private PlaylistService playlistService;

    @Qualifier("OpenWeatherMap")
    @Autowired
    TemperatureService temperatureServiceOpenWeather;

    @Qualifier("TemperatureSimulateFail")
    @Autowired
    TemperatureService temperatureServiceFail;

    @GetMapping("/playlist")
    @Async
    public CompletableFuture<PlaylistResponse> getMusicByTemperature(@RequestParam("city") String city) {
        return temperatureService.getTemperatureByCity(city)
                .thenApply(temperature -> {
                    log.info("city: {}", city);
                    log.info("temperature: {}", temperature);
                    return playlistService.getMusicByTemperature(temperature);
                })
                .thenApply(playlist -> {
                    log.info("playlist: {}", playlist);
                    return new PlaylistResponse(playlist);
                });
    }

    @PostMapping("/changeTemperatureService")
    public ResponseEntity<String> changeTemperatureService(@RequestParam String service) {
        if ("real".equalsIgnoreCase(service)) {
            this.temperatureService = temperatureServiceOpenWeather;
            return ResponseEntity.ok("changed!");
        } else if ("failed".equalsIgnoreCase(service)) {
            this.temperatureService = temperatureServiceFail;
            return ResponseEntity.ok("changed!");
        }
        return ResponseEntity.internalServerError().body("Error!");
    }

    @PostConstruct
    public void postConstruct() {
        if (this.temperatureService == null)
            this.temperatureService = temperatureServiceOpenWeather;
    }
}
