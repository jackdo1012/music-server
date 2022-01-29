package com.music.server.service;

import com.music.server.domain.Artist;
import com.music.server.domain.Music;
import com.music.server.repo.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {
    ArtistRepository artistRepository;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;

    }

    @Override
    public Artist addArtist(String name, List<Music> musics) {
        Artist newArtist = new Artist(name, musics);
        artistRepository.save(newArtist);
        return newArtist;
    }

    @Override
    public Artist addSongToArtist(String name, Music music) {
        Artist artist = artistRepository.findByName(name);

        if (artist == null) {
            artist = this.addArtist(name, new ArrayList<Music>());
        }

        artist.addSongs(music);
        artistRepository.save(artist);
        return artist;
    }
}
