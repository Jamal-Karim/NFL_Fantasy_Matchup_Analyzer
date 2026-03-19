package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupAnalyzer;
import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.domain.scoring.ScareResultFactory;
import com.jamalkarim.analyzer.dto.response.PlayerMatchupResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import com.jamalkarim.analyzer.repository.PlayerMatchupRepository;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.utils.PlayerMatchupMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerMatchupServiceTest {

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private PlayerMatchupRepository matchupRepository;
    @Mock
    private PlayerMatchupMapper mapper;
    @Mock
    private PlayerMatchupAnalyzer analyzer;
    @Mock
    private ScareResultFactory factory;

    @InjectMocks
    private PlayerMatchupService matchupService;

    private Player player1;
    private Player player2;
    private PlayerMatchupResult matchupResult;
    private PlayerMatchupResultEntity matchupEntity;
    private PlayerMatchupResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        matchupResult = mock(PlayerMatchupResult.class);
        matchupEntity = new PlayerMatchupResultEntity();
        matchupEntity.setId(1L);
        responseDTO = new PlayerMatchupResponseDTO();
    }

    @Test
    void createPlayerMatchupResponseById_Success() {
        when(matchupRepository.findById(1L)).thenReturn(Optional.of(matchupEntity));
        when(mapper.entityToDomain(matchupEntity)).thenReturn(matchupResult);

        when(matchupResult.getPlayer1()).thenReturn(player1);
        when(matchupResult.getPlayer2()).thenReturn(player2);

        ScareResult scare1 = mock(ScareResult.class);
        ScareResult scare2 = mock(ScareResult.class);
        when(factory.generateScareResult(player1)).thenReturn(scare1);
        when(factory.generateScareResult(player2)).thenReturn(scare2);

        when(mapper.domainToResponse(matchupResult)).thenReturn(responseDTO);

        PlayerMatchupResponseDTO result = matchupService.getPlayerMatchupResponseById(1L);

        assertNotNull(result);
        verify(matchupRepository).findById(1L);
    }

    @Test
    void getPlayerMatchupResponseById_NotFound() {
        when(matchupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(com.jamalkarim.analyzer.exceptions.MatchupNotFoundException.class, () -> matchupService.getPlayerMatchupResponseById(1L));
    }

    @Test
    void createPlayerMatchup_Success() {
        when(player1.getName()).thenReturn("Player 1");
        when(player1.getTeam()).thenReturn("Team 1");
        when(player2.getName()).thenReturn("Player 2");
        when(player2.getTeam()).thenReturn("Team 2");

        when(analyzer.analyzePlayerMatchup(player1, player2)).thenReturn(matchupResult);
        when(mapper.domainToEntity(matchupResult)).thenReturn(matchupEntity);

        when(playerRepository.findByNameAndNflTeam("Player 1", "Team 1")).thenReturn(Optional.empty());
        when(playerRepository.findByNameAndNflTeam("Player 2", "Team 2")).thenReturn(Optional.empty());

        when(matchupRepository.save(any(PlayerMatchupResultEntity.class))).thenReturn(matchupEntity);
        when(mapper.domainToResponse(matchupResult)).thenReturn(responseDTO);

        PlayerMatchupResponseDTO result = matchupService.createPlayerMatchup(player1, player2);

        assertNotNull(result);
        verify(matchupRepository).save(any(PlayerMatchupResultEntity.class));
    }
}
