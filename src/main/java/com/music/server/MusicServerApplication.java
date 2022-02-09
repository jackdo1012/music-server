package com.music.server;

import com.music.server.domain.PlayingSongMessage;
import com.music.server.service.YoutubeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class MusicServerApplication {
    YoutubeServiceImpl youtubeService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MusicServerApplication(YoutubeServiceImpl youtubeService, SimpMessagingTemplate simpMessagingTemplate) {
        this.youtubeService = youtubeService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(MusicServerApplication.class, args);
    }

    private void initVidList() {
        System.out.println("Queue is empty, adding new songs");
        youtubeService.getMusicQueue().set(youtubeService.initList());
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Timer interval = new Timer();

            interval.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    LocalDateTime now = LocalDateTime.now();
                    Duration difference = Duration.between(youtubeService.getStartTime(), now);

                    if (youtubeService.getCurrentSong() != null && difference.compareTo(youtubeService.getCurrentSong().getDuration()) >= 0 && youtubeService.getMusicQueue().isEmpty()) {
                        youtubeService.setCurrentSong(null);
                    }
                    if ((youtubeService.getCurrentSong() == null || difference.compareTo(youtubeService.getCurrentSong().getDuration()) >= 0) && !youtubeService.getMusicQueue().isEmpty()) {
                        System.out.println("Song dequeue from list");
                        youtubeService.setCurrentSong(youtubeService.getMusicQueue().peek());
                        youtubeService.getMusicQueue().dequeue();
                        youtubeService.setStartTime(now);
                        System.out.println("Current song: " + youtubeService.getCurrentSong().getName());
                        simpMessagingTemplate.convertAndSend("/topic/playingVideo", new PlayingSongMessage(youtubeService.getCurrentSong(), youtubeService.getPlayedTime().getSeconds()));
                    }

                    if (youtubeService.getMusicQueue().isEmpty()) {
                        initVidList();
                    }
                }

            }, 0, 2000);
        };
    }

}
