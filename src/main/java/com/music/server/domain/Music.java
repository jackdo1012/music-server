package com.music.server.domain;

import javax.persistence.*;

import java.net.MalformedURLException;
import java.net.URL;
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
                String vidId = map.get("v");
                this.vidId = vidId;
            } else if (host.contains("youtu.be")) {
                String path = urlParsed.getPath();
                String vidId = path.split("/")[1];
                this.vidId = vidId;
            }
        } catch (MalformedURLException e) {
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
    }

    public String getVidId() {
        return vidId;
    }

    public void setVidId(String vidId) {
        this.vidId = vidId;
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
