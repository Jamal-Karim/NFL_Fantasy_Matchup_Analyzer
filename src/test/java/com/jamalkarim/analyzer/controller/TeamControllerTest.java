package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
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
import static org.mockito.Mockito.mock;
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
    void getAllTeams_Success() throws Exception {
        java.util.List<TeamResponseDTO> teams = java.util.Collections.singletonList(new TeamResponseDTO());
        org.springframework.data.domain.Page<TeamResponseDTO> teamPage = new org.springframework.data.domain.PageImpl<>(teams);
        when(teamService.getAllTeams(0, 10)).thenReturn(teamPage);

        mockMvc.perform(get("/api/team")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void updateTeam_Success() throws Exception {
        TeamRequest request = new TeamRequest();
        request.setName("Updated Team");

        TeamResponseDTO responseDTO = new TeamResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Updated Team");

        when(teamService.updateTeam(eq(1L), any(TeamRequest.class))).thenReturn(responseDTO);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/team/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.name").value("Updated Team"));
    }

    @Test
    void deleteTeam_Success() throws Exception {
        when(teamService.deleteTeam(1L)).thenReturn("Successfully deleted team Test Team");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/team/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data").value("Successfully deleted team Test Team"));
    }
}
