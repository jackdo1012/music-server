package com.music.server.controller;

import com.music.server.domain.Artist;
import com.music.server.domain.Music;
import com.music.server.repo.ArtistRepository;
import com.music.server.repo.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    ArtistRepository artistRepository;
    MusicRepository musicRepository;

    @Autowired
    public HomeController(ArtistRepository artistRepository, MusicRepository musicRepository) {
        this.artistRepository = artistRepository;
        this.musicRepository = musicRepository;
    }

    @GetMapping
    public String home(Model model, HttpServletRequest request) {
        boolean auth = (boolean) request.getAttribute("auth");
        List<Artist> artists = new ArrayList<>(artistRepository.findAll());
        List<Music> musics = new ArrayList<>(musicRepository.findAll());
        model.addAttribute("artists", artists);
        model.addAttribute("musics", musics);
        if (auth) {
            model.addAttribute("musicForm", new Music());
        }
        model.addAttribute("auth", auth);
        return "home";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, HttpServletRequest request, @PathVariable("id") UUID id) {
        boolean auth = (boolean) request.getAttribute("auth");
        if (!auth) {
            return "redirect:/";
        }
        if (musicRepository.existsById(id)) {
            Music music = musicRepository.getById(id);
            model.addAttribute("music", music);
            return "edit";
        } else {
            return "redirect:/";
        }

    }
}