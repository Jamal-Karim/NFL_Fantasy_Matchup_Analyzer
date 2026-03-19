package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.requests.TeamMatchupRequest;
import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.TeamMatchupResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import com.jamalkarim.analyzer.service.TeamMatchupService;
import com.jamalkarim.analyzer.service.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
public class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private TeamMatchupService teamMatchupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getTeamById_Success() throws Exception {
        TeamResponseDTO responseDTO = new TeamResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Test Team");

        when(teamService.getTeamResponseById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/team/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test Team"));
    }

    @Test
    void createTeam_Success() throws Exception {
        TeamRequest request = new TeamRequest();
        request.setName("New Team");

        TeamResponseDTO responseDTO = new TeamResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("New Team");

        when(teamService.createTeam(any(TeamRequest.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/team/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.name").value("New Team"));
    }

    @Test
    void createTeamMatchupResult_Success() throws Exception {
        TeamMatchupRequest request = new TeamMatchupRequest();
        request.setTeam1Id(1L);
        request.setTeam2Id(2L);

        Team team1 = new Team("Team 1");
        Team team2 = new Team("Team 2");

        TeamMatchupResponseDTO responseDTO = new TeamMatchupResponseDTO();
        responseDTO.setTeam1("Team 1");
        responseDTO.setTeam2("Team 2");

        when(teamService.getTeamById(1L)).thenReturn(team1);
        when(teamService.getTeamById(2L)).thenReturn(team2);
        when(teamMatchupService.createTeamMatchup(eq(team1), eq(team2))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/team/matchup/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.team_1").value("Team 1"))
                .andExpect(jsonPath("$.data.team_2").value("Team 2"));
    }

    @Test
    void getTeamMatchupById_Success() throws Exception {
        TeamMatchupResponseDTO responseDTO = new TeamMatchupResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTeam1("Team 1");

        when(teamMatchupService.getTeamMatchupById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/team/matchup/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.team_1").value("Team 1"));
    }
}
