package com.music.server.domain;

public class PlayingSongMessage {
    private Music song;
    private long duration;

    public PlayingSongMessage(Music song, long duration) {
        this.song = song;
        this.duration = duration;
    }

    public Music getSong() {
        return song;
    }

    public void setSong(Music song) {
        this.song = song;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
