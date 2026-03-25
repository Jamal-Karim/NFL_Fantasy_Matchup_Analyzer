package com.jamalkarim.analyzer.cucumber.utils;

import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Component
public class DbUtils {
    private final PlayerRepository playerRepository;

    public DbUtils(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void verifyPlayerIsSaved(String name) {
        Optional<PlayerEntity> player = playerRepository.findByName(name);
        assertThat(player)
                .withFailMessage("Expected player %s to be in the database, but was not.", name)
                .isPresent();
    }
}
