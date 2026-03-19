package com.jamalkarim.analyzer;

import com.jamalkarim.analyzer.dto.requests.PlayerRequest;
import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
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

    @Test
    void createTeam_Integration() {
        TeamRequest request = new TeamRequest();
        request.setName("Dream Team");

        List<PlayerRequest> roster = new ArrayList<>();
        PlayerRequest p1 = new PlayerRequest();
        p1.setName("Patrick Mahomes");
        p1.setTeam("KC");
        roster.add(p1);

        PlayerRequest p2 = new PlayerRequest();
        p2.setName("Christian McCaffrey");
        p2.setTeam("SF");
        roster.add(p2);

        request.setRoster(roster);

        TeamResponseDTO createdTeam = teamService.createTeam(request);

        assertNotNull(createdTeam);
        assertNotNull(createdTeam.getId());
        assertEquals("Dream Team", createdTeam.getName());
        assertEquals(2, createdTeam.getRoster().size());

        TeamResponseDTO retrievedTeam = teamService.getTeamResponseById(createdTeam.getId());
        assertEquals("Dream Team", retrievedTeam.getName());
        assertEquals(2, retrievedTeam.getRoster().size());
    }

    @Test
    void createTeam_AlreadyExists() {
        TeamRequest request = new TeamRequest();
        request.setName("Existing Team");
        request.setRoster(new ArrayList<>());

        teamService.createTeam(request);

        TeamResponseDTO secondAttempt = teamService.createTeam(request);

        assertNotNull(secondAttempt);
        assertEquals("Existing Team", secondAttempt.getName());
    }
}
