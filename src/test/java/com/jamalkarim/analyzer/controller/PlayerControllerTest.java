package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.service.PlayerService;
import com.jamalkarim.analyzer.service.ScareResultService;
import com.jamalkarim.analyzer.service.SimulationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerService playerService;

    @MockitoBean
    private SimulationService simulationService;

    @MockitoBean
    private ScareResultService scareResultService;

    @Test
    void getPlayerByName_Success() throws Exception {
        PlayerResponseDTO responseDTO = new PlayerResponseDTO();
        responseDTO.setName("Patrick Mahomes");

        when(playerService.getOrSyncPlayer("Patrick Mahomes", "KC")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/player/team/KC")
                        .param("name", "Patrick Mahomes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.name").value("Patrick Mahomes"));
    }

    @Test
    void getPlayerById_Success() throws Exception {
        PlayerResponseDTO responseDTO = new PlayerResponseDTO();
        responseDTO.setName("Patrick Mahomes");

        when(playerService.getPlayerResponseDTOByID(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/player/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.data.name").value("Patrick Mahomes"));
    }

    @Test
    void getAllPlayers_Success() throws Exception {
        java.util.List<PlayerResponseDTO> players = java.util.Collections.singletonList(new PlayerResponseDTO());
        org.springframework.data.domain.Page<PlayerResponseDTO> playerPage = new org.springframework.data.domain.PageImpl<>(players);
        when(playerService.getAllPlayers(null, 0, 10)).thenReturn(playerPage);

        mockMvc.perform(get("/api/player")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }
}
