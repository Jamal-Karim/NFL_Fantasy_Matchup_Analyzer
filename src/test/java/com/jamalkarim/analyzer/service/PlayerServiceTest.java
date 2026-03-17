package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.domain.scoring.ScareResultFactory;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.ScareResultEntity;
import com.jamalkarim.analyzer.exceptions.PlayerNotFoundException;
import com.jamalkarim.analyzer.provider.PlayerDataProvider;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository repository;
    @Mock
    private PlayerDataProvider provider;
    @Mock
    private PlayerMapper playerMapper;
    @Mock
    private ScareResultMapper scareResultMapper;
    @Mock
    private ScareResultFactory factory;

    @InjectMocks
    private PlayerService playerService;

    private PlayerEntity playerEntity;
    private Player player;
    private PlayerResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        playerEntity = new PlayerEntity();
        playerEntity.setId(1L);
        playerEntity.setName("Patrick Mahomes");
        playerEntity.setNflTeam("KC");

        player = mock(Player.class);

        responseDTO = new PlayerResponseDTO();
        responseDTO.setName("Patrick Mahomes");
    }

    @Test
    void getPlayerResponseDTOByID_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(playerEntity));
        when(playerMapper.entityToDomain(playerEntity)).thenReturn(player);
        when(playerMapper.domainToResponse(player)).thenReturn(responseDTO);

        PlayerResponseDTO result = playerService.getPlayerResponseDTOByID(1L);

        assertNotNull(result);
        assertEquals("Patrick Mahomes", result.getName());
        verify(repository).findById(1L);
    }

    @Test
    void getPlayerResponseDTOByID_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> playerService.getPlayerResponseDTOByID(1L));
    }

    @Test
    void getOrSyncPlayer_AlreadyExists() {
        when(repository.findByNameAndNflTeam("Patrick Mahomes", "KC")).thenReturn(Optional.of(playerEntity));
        when(playerMapper.entityToDomain(playerEntity)).thenReturn(player);
        when(playerMapper.domainToResponse(player)).thenReturn(responseDTO);

        PlayerResponseDTO result = playerService.getOrSyncPlayer("Patrick Mahomes", "KC");

        assertNotNull(result);
        assertEquals("Patrick Mahomes", result.getName());
        verify(provider, never()).fetchPlayer(any(), any());
    }

    @Test
    void getOrSyncPlayer_SyncFromProvider() {
        when(repository.findByNameAndNflTeam("Patrick Mahomes", "KC")).thenReturn(Optional.empty());
        when(provider.fetchPlayer("Patrick Mahomes", "KC")).thenReturn(player);
        when(playerMapper.domainToEntity(player)).thenReturn(playerEntity);
        
        ScareResult scareResult = mock(ScareResult.class);
        when(factory.generateScareResult(player)).thenReturn(scareResult);
        
        ScareResultEntity scareEntity = new ScareResultEntity();
        when(scareResultMapper.scareDomainToScareEntity(scareResult)).thenReturn(scareEntity);
        when(repository.save(any(PlayerEntity.class))).thenReturn(playerEntity);
        when(playerMapper.domainToResponse(player)).thenReturn(responseDTO);

        PlayerResponseDTO result = playerService.getOrSyncPlayer("Patrick Mahomes", "KC");

        assertNotNull(result);
        assertEquals("Patrick Mahomes", result.getName());
        verify(repository).save(any(PlayerEntity.class));
    }

    @Test
    void getOrSyncPlayer_NotFoundInProvider() {
        when(repository.findByNameAndNflTeam("Patrick Mahomes", "KC")).thenReturn(Optional.empty());
        when(provider.fetchPlayer("Patrick Mahomes", "KC")).thenReturn(null);

        assertThrows(PlayerNotFoundException.class, () -> playerService.getOrSyncPlayer("Patrick Mahomes", "KC"));
    }
}
