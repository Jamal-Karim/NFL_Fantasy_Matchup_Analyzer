package com.jamalkarim.analyzer;

import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PlayerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void getOrSyncPlayer_Integration() throws Exception {
        // 1. Initial request - should sync from mock data
        mockMvc.perform(get("/api/player/team/KC")
                .param("name", "Patrick Mahomes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.name").value("Patrick Mahomes"));

        // 2. Verify it's in the database
        assert(playerRepository.findByNameAndNflTeam("Patrick Mahomes", "KC").isPresent());

        // 3. Second request - should find in database
        mockMvc.perform(get("/api/player/team/KC")
                .param("name", "Patrick Mahomes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.name").value("Patrick Mahomes"));
    }
}
