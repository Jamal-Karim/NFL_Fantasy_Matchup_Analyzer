package com.jamalkarim.analyzer.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamalkarim.analyzer.service.PlayerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PlayerDataInitializer implements CommandLineRunner {

    private final PlayerService playerService;
    private final ObjectMapper objectMapper;

    public PlayerDataInitializer(PlayerService playerService, ObjectMapper objectMapper) {
        this.playerService = playerService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        ClassPathResource resource = new ClassPathResource("seeding/players.json");

        List<Map<String, String>> seeds = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<>() {
                }
        );

        for (Map<String, String> seed : seeds) {
            playerService.getOrSyncPlayer(seed.get("name"), seed.get("nflTeam"));
        }
    }
}
