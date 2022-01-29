package com.music.server.controller;

import com.music.server.domain.Artist;
import com.music.server.repo.ArtistRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = { "http://localhost:8080", "https://music.jackdo1012.tk", "https://music-server.jackdo1012.tk" })
@RestController
@RequestMapping("/api/artists")
public class ArtistController {
    ArtistRepository artistRepository;

    @Autowired
    public ArtistController(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getArtist() {
        try {
            List<Artist> artists = new ArrayList<>(artistRepository.findAll());

            if (artists.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(artists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<Artist>> getArtistByName(@PathVariable String name) {
        try {
            List<Artist> artists = new ArrayList<>(artistRepository.findByNameIsContainingAllIgnoreCase(name));
            if (artists.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(artists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
