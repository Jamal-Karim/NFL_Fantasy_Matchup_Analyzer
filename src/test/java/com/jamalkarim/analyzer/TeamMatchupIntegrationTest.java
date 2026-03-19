package com.jamalkarim.analyzer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.dto.requests.PlayerRequest;
import com.jamalkarim.analyzer.dto.requests.TeamMatchupRequest;
import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.repository.TeamMatchupRepository;
import com.jamalkarim.analyzer.service.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TeamMatchupIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamService teamService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamMatchupRepository teamMatchupRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTeamMatchup_EndToEnd_Success() throws Exception {
        String teamName1 = "Chiefs Kingdom " + System.currentTimeMillis();
        String teamName2 = "Raven Nation " + System.currentTimeMillis();
        
        long now = System.currentTimeMillis();
        Position[] positions = {Position.QB, Position.RB, Position.WR};
        
        // Create 3 players for Team 1
        List<PlayerRequest> roster1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String name = "Test Player 1-" + i + " " + now;
            playerRepository.save(TestUtils.createTestPlayer(name, "KC", positions[i]));
            PlayerRequest pr = new PlayerRequest();
            pr.setName(name);
            pr.setTeam("KC");
            roster1.add(pr);
        }

        // Create 3 players for Team 2
        List<PlayerRequest> roster2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String name = "Test Player 2-" + i + " " + now;
            playerRepository.save(TestUtils.createTestPlayer(name, "BAL", positions[i]));
            PlayerRequest pr = new PlayerRequest();
            pr.setName(name);
            pr.setTeam("BAL");
            roster2.add(pr);
        }

        // 1. Create Team 1
        TeamRequest teamRequest1 = new TeamRequest();
        teamRequest1.setName(teamName1);
        teamRequest1.setRoster(roster1);
        TeamResponseDTO team1 = teamService.createTeam(teamRequest1);

        // 2. Create Team 2
        TeamRequest teamRequest2 = new TeamRequest();
        teamRequest2.setName(teamName2);
        teamRequest2.setRoster(roster2);
        TeamResponseDTO team2 = teamService.createTeam(teamRequest2);

        // 3. Create Matchup via Controller
        TeamMatchupRequest matchupRequest = new TeamMatchupRequest();
        matchupRequest.setTeam1Id(team1.getId());
        matchupRequest.setTeam2Id(team2.getId());

        mockMvc.perform(post("/api/matchup/team/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matchupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.team_1").value(teamName1))
                .andExpect(jsonPath("$.data.team_2").value(teamName2))
                .andExpect(jsonPath("$.data.id").exists());

        // 4. Verify in Repository
        assertTrue(teamMatchupRepository.findByTeam1AndTeam2(teamName1, teamName2).isPresent());

        // 5. Get Matchup by ID
        long matchupId = teamMatchupRepository.findByTeam1AndTeam2(teamName1, teamName2).get().getId();

        mockMvc.perform(get("/api/matchup/team/" + matchupId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(matchupId))
                .andExpect(jsonPath("$.data.team_1").value(teamName1));
    }
}
