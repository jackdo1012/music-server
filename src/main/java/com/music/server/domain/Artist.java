package com.music.server.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Music> songs = new ArrayList<>();

    public Artist() {

    }

    public Artist(String name, List<Music> songs) {
        this.name = name;
        this.songs = songs;
    }

    public void addSongs(Music music) {
        this.songs.add(music);
    }

    public void removeSong(Music music) {
        this.songs.remove(music);
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

    public List<Music> getSongs() {
        return songs;
    }

    public void setSongs(List<Music> songs) {
        this.songs = songs;
    }
}
