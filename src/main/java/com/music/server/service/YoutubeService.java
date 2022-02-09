package com.music.server.service;

import com.google.api.services.youtube.model.Video;
import com.music.server.domain.Music;
import com.music.server.utils.QueueImpl;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public interface YoutubeService {
    Video getYoutubeData(String videoId) throws IOException;

    QueueImpl<Music> initList();

    QueueImpl<Music> getMusicQueue();

    LocalDateTime getStartTime();

    void setStartTime(LocalDateTime date);

    Music getCurrentSong();

    void setCurrentSong(Music currentSong);

    Duration getPlayedTime();
}
