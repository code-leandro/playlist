package com.leandrosouza.playlist.controller;

import com.leandrosouza.playlist.api.PlaylistResponse;
import com.leandrosouza.playlist.model.Playlist;
import com.leandrosouza.playlist.service.PlaylistService;
import com.leandrosouza.playlist.service.TemperatureService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@EnableAsync
@Slf4j
public class AppController {

    @Autowired
    private TemperatureService temperatureService;

    @Autowired
    private PlaylistService playlistService;

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
}
