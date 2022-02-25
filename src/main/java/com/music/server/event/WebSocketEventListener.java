package com.music.server.event;

import com.music.server.domain.PlayingSongMessage;
import com.music.server.service.YoutubeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WebSocketEventListener {
    YoutubeServiceImpl youtubeService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketEventListener(YoutubeServiceImpl youtubeService, SimpMessagingTemplate simpMessagingTemplate) {
        this.youtubeService = youtubeService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        String sessionId = SimpMessageHeaderAccessor.wrap(event.getMessage()).getSessionId();
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        if (sessionId != null) {
            simpMessagingTemplate.convertAndSendToUser(sessionId, "/topic/playingVideo", new PlayingSongMessage(youtubeService.getCurrentSong(), youtubeService.getPlayedTime().getSeconds()), headerAccessor.getMessageHeaders());
        }
    }
}
