package com.leandrosouza.playlist.service;

import com.leandrosouza.playlist.model.Playlist;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    @Override
    public Playlist getMusicByTemperature(int temperature) {
        if (temperature > 25) {
            return new Playlist("Pop", Arrays.asList("Hit 1", "Hit 2", "Hit 3"));
        } else if (temperature >= 10) {
            return new Playlist("Rock", Arrays.asList("Song 1", "Song 2", "Song 3"));
        } else {
            return new Playlist("Classic", Arrays.asList("Classical 1", "Classical 2", "Classical 3"));
        }
    }
}
