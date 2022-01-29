package com.music.server.controller;

import com.music.server.domain.Artist;
import com.music.server.domain.Music;
import com.music.server.repo.ArtistRepository;
import com.music.server.repo.MusicRepository;

import com.music.server.service.ArtistServiceImpl;
import com.music.server.service.MusicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/musics")
public class MusicController {
    MusicRepository musicRepository;
    ArtistRepository artistRepository;
    ArtistServiceImpl artistService;
    MusicServiceImpl musicService;

    @Autowired
    public MusicController(MusicRepository musicRepository, ArtistRepository artistRepository, ArtistServiceImpl artistService, MusicServiceImpl musicService) {
        this.musicRepository = musicRepository;
        this.artistRepository = artistRepository;
        this.artistService = artistService;
        this.musicService = musicService;
    }

    @GetMapping
    public ResponseEntity<List<Music>> getAllMusic() {
        try {
            List<Music> musics = new ArrayList<>(musicRepository.findAll());

            if (musics.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(musics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Music> createMusic(@RequestBody Music music, HttpServletRequest request) {
        boolean auth = (boolean) request.getAttribute("auth");
        if (!auth) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            Music _music = musicRepository
                    .save(new Music(music.getName(), music.getArtist(), music.getUrl()));
            artistService.addSongToArtist(music.getArtist(), _music);

            return new ResponseEntity<>(_music, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAll(HttpServletRequest request) {
        boolean auth = (boolean) request.getAttribute("auth");
        if (!auth) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            musicRepository.deleteAll();
            artistRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Music> getMusicById(@PathVariable("id") UUID id) {
        Optional<Music> musicData = musicRepository.findById(id);

        if (musicData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(musicData.get(), HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Music> updateMusic(@PathVariable("id") UUID id, @RequestBody Music music,
                                             HttpServletRequest request) {
        boolean auth = (boolean) request.getAttribute("auth");
        if (!auth) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Music musicAfterChange = musicService.editById(id, music.getName(), music.getArtist(), music.getUrl());
        if (musicAfterChange != null) {
            return new ResponseEntity<>(musicAfterChange, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") UUID id, HttpServletRequest request) {
        try {
            boolean auth = (boolean) request.getAttribute("auth");
            if (!auth) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            boolean deleteResult = musicService.deleteById(id);
            if (!deleteResult) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
