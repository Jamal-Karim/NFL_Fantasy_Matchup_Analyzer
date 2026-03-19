package com.jamalkarim.analyzer;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.dto.requests.PlayerRequest;
import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.service.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TeamIntegrationTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void createTeam_Integration() {
        String playerName = "Test QB " + System.currentTimeMillis();
        playerRepository.save(TestUtils.createTestPlayer(playerName, "KC", Position.QB));

        TeamRequest request = new TeamRequest();
        request.setName("Unique Dream Team " + System.currentTimeMillis());

        List<PlayerRequest> roster = new ArrayList<>();
        PlayerRequest p1 = new PlayerRequest();
        p1.setName(playerName);
        p1.setTeam("KC");
        roster.add(p1);

        request.setRoster(roster);

        TeamResponseDTO createdTeam = teamService.createTeam(request);

        assertNotNull(createdTeam);
        assertNotNull(createdTeam.getId());
        assertEquals(request.getName(), createdTeam.getName());
    }

    @Test
    void createTeam_AlreadyExists() {
        String teamName = "Existing Team " + System.currentTimeMillis();
        TeamRequest request = new TeamRequest();
        request.setName(teamName);
        request.setRoster(new ArrayList<>());

        teamService.createTeam(request);

        assertThrows(com.jamalkarim.analyzer.exceptions.TeamAlreadyExistsException.class, () -> {
            teamService.createTeam(request);
        });
    }
}
