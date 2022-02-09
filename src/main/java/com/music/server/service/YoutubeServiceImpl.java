package com.music.server.service;

import com.music.server.domain.Music;
import com.music.server.repo.MusicRepository;
import com.music.server.utils.QueueImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;


@Service
public class YoutubeServiceImpl implements YoutubeService {

    private final QueueImpl<Music> songsQueue;
    private final String youtubeApiKey = System.getenv("YOUTUBE_API_KEY");
    private LocalDateTime startTime;

    private Music currentSong;
    MusicRepository musicRepository;

    @Autowired
    public YoutubeServiceImpl(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
        this.songsQueue = initList();
        this.startTime = LocalDateTime.now();
    }

    @Override
    public QueueImpl<Music> initList() {
        QueueImpl<Music> songsQueue = new QueueImpl<>();
        List<Music> musics = musicRepository.findAll();
        musics.forEach(songsQueue::enqueue);
        return songsQueue;
    }

    @Override
    public Video getYoutubeData(String videoId) throws IOException {
        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) {
            }
        }).setApplicationName("jd-music").build();
        YouTube.Videos.List videoRequest = youtube.videos().list(List.of("snippet", "contentDetails"));
        videoRequest.setId(List.of(videoId));
        videoRequest.setKey(youtubeApiKey);
        VideoListResponse listResponse = videoRequest.execute();
        return listResponse.getItems().get(0);
    }

    @Override
    public QueueImpl<Music> getMusicQueue() {
        return songsQueue;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public Music getCurrentSong() {
        return currentSong;
    }

    @Override
    public void setCurrentSong(Music currentSong) {
        this.currentSong = currentSong;
    }

    @Override
    public Duration getPlayedTime() {
        return Duration.between(this.startTime, LocalDateTime.now());
    }
}
