package com.music.server.repo;

import com.music.server.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, UUID> {
    Artist findByName(String name);

    boolean existsById(UUID id);

    List<Artist> findByNameIsContainingAllIgnoreCase(String name);
}
