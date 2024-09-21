package com.leandrosouza.playlist.service;

import java.util.concurrent.CompletableFuture;

public interface TemperatureService {
    CompletableFuture<Integer> getTemperatureByCity(String city);
}
