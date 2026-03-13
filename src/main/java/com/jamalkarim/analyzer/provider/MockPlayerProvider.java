package com.jamalkarim.analyzer.provider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamalkarim.analyzer.dto.mock.MockPlayerDTO;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Repository
public class MockPlayerProvider implements PlayerDataProvider {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public MockPlayerDTO fetchPlayer(String name, String nflTeam) {
        String[] jsonFiles = {"mock_data/qb.json", "mock_data/rb.json", "mock_data/wr.json", "mock_data/te.json"};

        for (String fileName : jsonFiles) {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {

                if (inputStream == null) {
                    System.out.println("Could not find file: " + fileName);
                    continue;
                }

                List<MockPlayerDTO> players = mapper.readValue(inputStream, new TypeReference<List<MockPlayerDTO>>() {
                });

                for (MockPlayerDTO player : players) {
                    if (player.getName().equalsIgnoreCase(name) &&
                            player.getNflTeam().equalsIgnoreCase(nflTeam)) {
                        return player;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading " + fileName + ": " + e.getMessage());
            }
        }

        return null;
    }
}
