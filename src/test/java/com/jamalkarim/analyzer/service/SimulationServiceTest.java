package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.dto.response.SimulationResponseDTO;
import com.jamalkarim.analyzer.utils.ScareResultMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SimulationServiceTest {

    @Mock
    private ScareResultMapper scareResultMapper;

    @Mock
    private ScareResultService scareResultService;

    @InjectMocks
    private SimulationService simulationService;

    private ScareResponseDTO scareResponseDTO;
    private SimulationResponseDTO simulationResponseDTO;

    @BeforeEach
    void setUp() {
        scareResponseDTO = new ScareResponseDTO();
        scareResponseDTO.setName("Josh Allen");
        scareResponseDTO.setTeam("BUF");
        scareResponseDTO.setPosition(Position.QB);
        scareResponseDTO.setScareScore(80.0);
        scareResponseDTO.setScareTier(PlayerTier.ELITE);

        simulationResponseDTO = new SimulationResponseDTO();
        simulationResponseDTO.setName("Josh Allen");
        simulationResponseDTO.setTeam("BUF");
        simulationResponseDTO.setPosition(Position.QB);
        simulationResponseDTO.setScareTier(PlayerTier.ELITE);
    }

    @Test
    void runSimulation_Success() {
        // Arrange
        long playerId = 1L;
        when(scareResultService.getScareResultById(playerId)).thenReturn(scareResponseDTO);
        when(scareResultMapper.responseToSimulationResponse(scareResponseDTO)).thenReturn(simulationResponseDTO);

        // Act
        SimulationResponseDTO result = simulationService.runSimulation(playerId);

        // Assert
        assertNotNull(result);
        assertEquals("Josh Allen", result.getName());
        assertEquals("BUF", result.getTeam());
        assertEquals(Position.QB, result.getPosition());
        assertEquals(PlayerTier.ELITE, result.getScareTier());

        // Verify that simulation metrics are populated (and within realistic ranges)
        assertTrue(result.getMeanScareScore() > 0, "Mean score should be positive");
        assertTrue(result.getFloorScore() <= result.getMeanScareScore(), "Floor should be <= Mean");
        assertTrue(result.getCeilingScore() >= result.getMeanScareScore(), "Ceiling should be >= Mean");
        assertTrue(result.getBoomPercentage() >= 0 && result.getBoomPercentage() <= 100, "Boom % should be between 0-100");
        assertTrue(result.getBustPercentage() >= 0 && result.getBustPercentage() <= 100, "Bust % should be between 0-100");

        verify(scareResultService).getScareResultById(playerId);
        verify(scareResultMapper).responseToSimulationResponse(scareResponseDTO);
    }

    @Test
    void runSimulation_HighVolatilityPosition() {
        // Arrange
        scareResponseDTO.setPosition(Position.WR); // WR has higher volatility (0.35 vs 0.25)
        simulationResponseDTO.setPosition(Position.WR);
        long playerId = 2L;
        when(scareResultService.getScareResultById(playerId)).thenReturn(scareResponseDTO);
        when(scareResultMapper.responseToSimulationResponse(scareResponseDTO)).thenReturn(simulationResponseDTO);

        // Act
        SimulationResponseDTO result = simulationService.runSimulation(playerId);

        // Assert
        assertNotNull(result);
        // Volatility check: High volatility players should have a wider gap between floor and ceiling
        double gap = result.getCeilingScore() - result.getFloorScore();
        assertTrue(gap > 0, "Gap should be positive for high volatility");
    }
}
