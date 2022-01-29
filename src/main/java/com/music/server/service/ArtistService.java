package com.music.server.service;

import com.music.server.domain.Artist;
import com.music.server.domain.Music;

import java.util.List;

public interface ArtistService {
    Artist addArtist(String name, List<Music> musics);

    Artist addSongToArtist(String name, Music music);
}
