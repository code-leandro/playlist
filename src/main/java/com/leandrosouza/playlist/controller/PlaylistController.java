package com.leandrosouza.playlist.controller;

import com.leandrosouza.playlist.api.PlaylistResponse;
import com.leandrosouza.playlist.service.PlaylistService;
import com.leandrosouza.playlist.service.TemperatureService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/playlist")
@EnableAsync
@Slf4j
public class PlaylistController {

    private TemperatureService temperatureService;

    @Autowired
    private PlaylistService playlistService;

    @Qualifier("OpenWeatherMap")
    @Autowired
    TemperatureService temperatureServiceOpenWeather;

    @Qualifier("TemperatureSimulateFail")
    @Autowired
    TemperatureService temperatureServiceFail;

    @GetMapping
    @Operation(summary = "Obter playlist por temperatura",
            description = "Retorna uma playlist baseada na temperatura atual da cidade fornecida.")
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

    @Operation(summary = "Simular falha do serviço de temperatura alternando o serviço",
            description = "Altera o serviço de temperatura para 'real' (padrão usando OpenWeather) ou 'failed' (para simular falhas no sistema).")
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
