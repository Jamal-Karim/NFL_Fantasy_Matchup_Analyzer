package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.domain.scoring.ScareResultFactory;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.exceptions.PlayerNotFoundException;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import com.jamalkarim.analyzer.utils.ScareResultMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScareResultServiceTest {

    @Mock
    private ScareResultFactory factory;
    @Mock
    private PlayerRepository repository;
    @Mock
    private PlayerMapper playerMapper;
    @Mock
    private ScareResultMapper scareResultMapper;

    @InjectMocks
    private ScareResultService scareResultService;

    private PlayerEntity playerEntity;
    private Player player;
    private ScareResult scareResult;
    private ScareResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        playerEntity = new PlayerEntity();
        playerEntity.setId(1L);
        player = mock(Player.class);
        scareResult = mock(ScareResult.class);
        responseDTO = new ScareResponseDTO();
    }

    @Test
    void getScareResultById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(playerEntity));
        when(playerMapper.entityToDomain(playerEntity)).thenReturn(player);
        when(factory.generateScareResult(player)).thenReturn(scareResult);
        when(scareResultMapper.domainToResponse(scareResult)).thenReturn(responseDTO);

        ScareResponseDTO result = scareResultService.getScareResultById(1L);

        assertNotNull(result);
        verify(repository).findById(1L);
    }

    @Test
    void getScareResultById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> scareResultService.getScareResultById(1L));
    }
}
