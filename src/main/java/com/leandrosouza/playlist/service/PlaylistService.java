package com.leandrosouza.playlist.service;

import com.leandrosouza.playlist.model.Playlist;

public interface PlaylistService {
    Playlist getMusicByTemperature(int temperature);
}
