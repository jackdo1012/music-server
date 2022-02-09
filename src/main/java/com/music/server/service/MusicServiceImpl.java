package com.music.server.service;

import com.google.api.services.youtube.model.Video;
import com.music.server.domain.Artist;
import com.music.server.domain.Music;
import com.music.server.repo.ArtistRepository;
import com.music.server.repo.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

@Service
public class MusicServiceImpl implements MusicService {
    MusicRepository musicRepository;
    ArtistRepository artistRepository;
    YoutubeServiceImpl youtubeService;
    ArtistServiceImpl artistService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MusicServiceImpl(MusicRepository musicRepository, ArtistRepository artistRepository, YoutubeServiceImpl youtubeService, SimpMessagingTemplate simpMessagingTemplate, ArtistServiceImpl artistService) {
        this.musicRepository = musicRepository;
        this.artistRepository = artistRepository;
        this.youtubeService = youtubeService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.artistService = artistService;
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
            } else {
                artistRepository.save(artist);
            }
            musicRepository.deleteById(id);
            for (int i = 0; i < youtubeService.getMusicQueue().toArray().size(); i++) {
                if (youtubeService.getMusicQueue().toArray().get(i).getId().equals(id)) {
                    youtubeService.getMusicQueue().removeAt(i);
                }
            }
            simpMessagingTemplate.convertAndSend("/topic/updateTrack", 1);
            return true;
        }
    }

    @Override
    public Music addNewMusic(String name, String artist, String url) {
        Music _music = new Music(name, artist, url);
        Video videos = null;
        try {
            videos = youtubeService.getYoutubeData(_music.getVidId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (videos != null) {
            _music.setDuration(Duration.parse(videos.getContentDetails().getDuration()));
            _music.setThumbnail(videos.getSnippet().getThumbnails().getMaxres().getUrl());
        }
        musicRepository.save(_music);
        artistService.addSongToArtist(_music.getArtist(), _music);
        youtubeService.getMusicQueue().enqueue(_music);
        simpMessagingTemplate.convertAndSend("/topic/updateTrack", 1);
        return _music;
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
                Video videos = null;
                try {
                    videos = youtubeService.getYoutubeData(_music.getVidId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (videos != null) {
                    _music.setDuration(Duration.parse(videos.getContentDetails().getDuration()));
                    _music.setThumbnail(videos.getSnippet().getThumbnails().getMaxres().getUrl());
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
            Music newMusic = musicRepository.save(_music);
            simpMessagingTemplate.convertAndSend("/topic/updateTrack", 1);
            return newMusic;
        } else {
            return null;
        }
    }
}
