package com.jamalkarim.analyzer.repository;

import com.jamalkarim.analyzer.entities.PlayerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void findByNameAndNflTeam_Success() {
        PlayerEntity entity = new PlayerEntity();
        entity.setName("Patrick Mahomes");
        entity.setNflTeam("KC");
        playerRepository.save(entity);

        Optional<PlayerEntity> found = playerRepository.findByNameAndNflTeam("Patrick Mahomes", "KC");

        assertTrue(found.isPresent());
        assertEquals("Patrick Mahomes", found.get().getName());
        assertEquals("KC", found.get().getNflTeam());
    }

    @Test
    void findByNameAndNflTeam_NotFound() {
        Optional<PlayerEntity> found = playerRepository.findByNameAndNflTeam("NonExistent", "NO");
        assertTrue(found.isEmpty());
    }
}
