package com.music.server.service;

import com.music.server.domain.Music;
import org.springframework.lang.Nullable;

import java.util.UUID;

public interface MusicService {
    boolean deleteById(UUID id);

    Music editById(UUID id, @Nullable String name, @Nullable String artist, @Nullable String url);
}
