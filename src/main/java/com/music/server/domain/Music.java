package com.music.server.domain;

import javax.persistence.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "musics")
public class Music {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "artist")
    private String artist;

    @Column(name = "vidId")
    private String vidId;

    @Column(name = "duration")
    private Duration duration;

    @Column(name = "thumbnail")
    private String thumbnail;

    public Music() {

    }

    public Music(String name, String artist, String url) {
        this.name = name;
        this.artist = artist;
        this.url = url;
        try {
            URL urlParsed = new URL(url);

            String host = urlParsed.getHost();
            if (host.contains("youtube.com")) {
                String[] params = urlParsed.getQuery().split("&");
                Map<String, String> map = new HashMap<>();

                for (String param : params) {
                    String key = param.split("=")[0];
                    String value = param.split(key + "=")[1];
                    map.put(key, value);
                }
                this.vidId = map.get("v");
            } else if (host.contains("youtu.be")) {
                String path = urlParsed.getPath();
                this.vidId = path.split("/")[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        URL urlParsed = null;
        try {
            urlParsed = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String host = null;
        if (urlParsed != null) {
            host = urlParsed.getHost();
            if (host.contains("youtube.com")) {
                String[] params = urlParsed.getQuery().split("&");
                Map<String, String> map = new HashMap<>();

                for (String param : params) {
                    String key = param.split("=")[0];
                    String value = param.split(key + "=")[1];
                    map.put(key, value);
                }
                this.vidId = map.get("v");
            } else if (host.contains("youtu.be")) {
                String path = urlParsed.getPath();
                this.vidId = path.split("/")[1];
            }
        }
    }

    public String getVidId() {
        return vidId;
    }

    public void setVidId(String vidId) {
        this.vidId = vidId;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
