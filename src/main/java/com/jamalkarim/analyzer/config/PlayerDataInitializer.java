package com.jamalkarim.analyzer.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamalkarim.analyzer.service.PlayerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Component that initializes player data when the application starts.
 * Seeds the database with players listed in a JSON file.
 */
@Component
@Profile({"!prod", "!test"})
public class PlayerDataInitializer implements CommandLineRunner {

    private final PlayerService playerService;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a PlayerDataInitializer with necessary dependencies.
     *
     * @param playerService The service for player operations
     * @param objectMapper  The Jackson object mapper for JSON processing
     */
    public PlayerDataInitializer(PlayerService playerService, ObjectMapper objectMapper) {
        this.playerService = playerService;
        this.objectMapper = objectMapper;
    }

    /**
     * Seeds player data from a JSON file when the application context is ready.
     *
     * @param args command line arguments
     * @throws Exception if an error occurs during seeding
     */
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
