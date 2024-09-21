package com.leandrosouza.playlist.api;

import com.leandrosouza.playlist.model.Playlist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistResponse {
    private String style;
    private List<String> songs;

    public PlaylistResponse(Playlist playlist) {
        this.style = playlist.getStyle();
        this.songs = playlist.getSongs();
    }
}

