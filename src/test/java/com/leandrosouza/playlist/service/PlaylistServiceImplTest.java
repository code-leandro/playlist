package com.leandrosouza.playlist.service;

import com.leandrosouza.playlist.service.impl.PlaylistServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.leandrosouza.playlist.model.Playlist;

class PlaylistServiceImplTest {

    private final PlaylistService playlistService = new PlaylistServiceImpl();

    @Test
    void testGetMusicByTemperatureAbove25() {

        int temperature = 30;
        Playlist playlist = playlistService.getMusicByTemperature(temperature);
        assertEquals("Pop", playlist.getStyle());
        Assertions.assertThat(playlist.getSongs())
                .isNotEmpty()
                .contains("Hit 1", "Hit 2", "Hit 3");
    }

    @Test
    void testGetMusicByTemperatureBetween10And25() {

        int temperature = 15;
        Playlist playlist = playlistService.getMusicByTemperature(temperature);
        assertEquals("Rock", playlist.getStyle());
        Assertions.assertThat(playlist.getSongs())
                .isNotEmpty()
                .contains("Song 1", "Song 2", "Song 3");
    }

    @Test
    void testGetMusicByTemperatureBelow10() {

        int temperature = 5;
        Playlist playlist = playlistService.getMusicByTemperature(temperature);
        assertEquals("Classic", playlist.getStyle());
        Assertions.assertThat(playlist.getSongs())
                .isNotEmpty()
                .contains("Classical 1", "Classical 2", "Classical 3");
    }
}