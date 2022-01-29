package com.music.server.service;

import com.music.server.domain.Artist;
import com.music.server.domain.Music;
import com.music.server.repo.ArtistRepository;
import com.music.server.repo.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class MusicServiceImpl implements MusicService {
    MusicRepository musicRepository;
    ArtistRepository artistRepository;

    @Autowired
    public MusicServiceImpl(MusicRepository musicRepository, ArtistRepository artistRepository) {
        this.musicRepository = musicRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public boolean deleteById(UUID id) {
        Optional<Music> music = musicRepository.findById(id);
        if (music.isEmpty()) {
            return false;
        } else {
            Artist artist = artistRepository.findByName(music.get().getArtist());

            artist.removeSong(music.get());
            if (artist.getSongs().size() == 0) {
                artistRepository.delete(artist);
            }
            musicRepository.deleteById(id);
            if (artist.getSongs().size() != 0) {
                artistRepository.save(artist);
            }
            return true;
        }

    }

    @Override
    public Music editById(UUID id, @Nullable String name, @Nullable String artist, @Nullable String url) {
        Optional<Music> musicData = musicRepository.findById(id);

        if (musicData.isPresent()) {
            Music _music = musicData.get();
            if (name != null)
                _music.setName(name);
            if (url != null) {
                _music.setUrl(url);
                URL urlParsed = null;
                try {
                    urlParsed = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (urlParsed != null) {

                    String host = urlParsed.getHost();
                    String vidId = _music.getVidId();
                    if (host.contains("youtube.com")) {
                        String[] params = urlParsed.getQuery().split("&");
                        Map<String, String> map = new HashMap<>();

                        for (String param : params) {
                            String key = param.split("=")[0];
                            String value = param.split(key + "=")[1];
                            map.put(key, value);
                        }
                        vidId = map.get("v");
                    } else if (host.contains("youtu.be")) {
                        String path = urlParsed.getPath();
                        vidId = path.split("/")[1];
                    }
                    _music.setVidId(vidId);
                }
            }
            if (artist != null) {
                Artist oldArtist = artistRepository.findByName(musicData.get().getArtist());
                oldArtist.removeSong(_music);
                if (oldArtist.getSongs().size() == 0) {
                    artistRepository.delete(oldArtist);
                }
                _music.setArtist(artist);
                Artist newArtist = artistRepository.findByName(artist);
                if (newArtist == null) {
                    newArtist = new Artist(artist, new ArrayList<>(List.of(_music)));
                } else {
                    newArtist.addSongs(_music);
                }
                artistRepository.save(newArtist);
            }
            return musicRepository.save(_music);
        } else {
            return null;
        }
    }
}
