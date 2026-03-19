package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.models.QuarterBack;
import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.requests.PlayerMatchupRequest;
import com.jamalkarim.analyzer.dto.requests.TeamMatchupRequest;
import com.jamalkarim.analyzer.dto.response.PlayerMatchupResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamMatchupResponseDTO;
import com.jamalkarim.analyzer.service.PlayerMatchupService;
import com.jamalkarim.analyzer.service.PlayerService;
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

@WebMvcTest(MatchupController.class)
public class MatchupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerService playerService;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private PlayerMatchupService matchupService;

    @MockitoBean
    private TeamMatchupService teamMatchupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPlayerMatchup_Success() throws Exception {
        PlayerMatchupRequest request = new PlayerMatchupRequest();
        request.setPlayer1Id(1L);
        request.setPlayer2Id(2L);

        Player p1 = new QuarterBack("P1", "T1");
        Player p2 = new QuarterBack("P2", "T2");

        PlayerMatchupResponseDTO responseDTO = new PlayerMatchupResponseDTO();
        responseDTO.setWinner("P1");

        when(playerService.getPlayerByID(1L)).thenReturn(p1);
        when(playerService.getPlayerByID(2L)).thenReturn(p2);
        when(matchupService.createPlayerMatchup(any(), any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/matchup/player/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.winner").value("P1"));
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
        when(teamMatchupService.createTeamMatchup(any(), any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/matchup/team/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.team_1").value("Team 1"))
                .andExpect(jsonPath("$.data.team_2").value("Team 2"));
    }

    @Test
    void getPlayerMatchupById_Success() throws Exception {
        PlayerMatchupResponseDTO responseDTO = new PlayerMatchupResponseDTO();
        responseDTO.setId(1L);

        when(matchupService.getPlayerMatchupResponseById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/matchup/player/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void getTeamMatchupById_Success() throws Exception {
        TeamMatchupResponseDTO responseDTO = new TeamMatchupResponseDTO();
        responseDTO.setId(1L);

        when(teamMatchupService.getTeamMatchupById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/matchup/team/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value(1));
    }
}
