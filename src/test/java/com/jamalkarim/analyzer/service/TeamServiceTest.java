package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.requests.PlayerRequest;
import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.TeamEntity;
import com.jamalkarim.analyzer.exceptions.TeamNotFoundException;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.repository.TeamRepository;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import com.jamalkarim.analyzer.utils.TeamMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository repository;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private TeamMapper mapper;
    @Mock
    private PlayerService playerService;
    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private TeamService teamService;

    private TeamEntity teamEntity;
    private Team teamDomain;
    private TeamRequest teamRequest;
    private TeamResponseDTO teamResponseDTO;

    @BeforeEach
    void setUp() {
        teamEntity = new TeamEntity();
        teamEntity.setId(1L);
        teamEntity.setName("San Francisco 49ers");

        teamDomain = new Team("San Francisco 49ers");
        teamDomain.setId(1L);

        teamRequest = new TeamRequest();
        teamRequest.setName("San Francisco 49ers");
        teamRequest.setRoster(new ArrayList<>());

        teamResponseDTO = new TeamResponseDTO();
        teamResponseDTO.setId(1L);
        teamResponseDTO.setName("San Francisco 49ers");
    }

    @Test
    void getTeamResponseById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(teamEntity));
        when(mapper.entityToDomain(teamEntity)).thenReturn(teamDomain);
        when(mapper.domainToResponse(teamDomain)).thenReturn(teamResponseDTO);

        TeamResponseDTO result = teamService.getTeamResponseById(1L);

        assertNotNull(result);
        assertEquals("San Francisco 49ers", result.getName());
        verify(repository).findById(1L);
    }

    @Test
    void getTeamResponseById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> teamService.getTeamResponseById(1L));
    }

    @Test
    void createTeam_AlreadyExists() {
        when(repository.findByName("San Francisco 49ers")).thenReturn(Optional.of(teamEntity));
        when(mapper.entityToDomain(teamEntity)).thenReturn(teamDomain);
        when(mapper.domainToResponse(teamDomain)).thenReturn(teamResponseDTO);

        TeamResponseDTO result = teamService.createTeam(teamRequest);

        assertNotNull(result);
        assertEquals("San Francisco 49ers", result.getName());
        verify(repository, never()).save(any());
    }

    @Test
    void createTeam_NewTeam_Success() {
        // Arrange
        when(repository.findByName("San Francisco 49ers")).thenReturn(Optional.empty());

        PlayerRequest pr = new PlayerRequest();
        pr.setName("Brock Purdy");
        pr.setTeam("SF");
        teamRequest.setRoster(List.of(pr));

        PlayerResponseDTO playerDTO = new PlayerResponseDTO();
        playerDTO.setId(10L);
        playerDTO.setName("Brock Purdy");

        Player playerModel = mock(Player.class);
        when(playerModel.getId()).thenReturn(10L);
        when(playerModel.getPosition()).thenReturn(com.jamalkarim.analyzer.domain.enums.Position.QB);

        when(playerService.getOrSyncPlayer("Brock Purdy", "SF")).thenReturn(playerDTO);
        when(playerMapper.responseToDomain(playerDTO)).thenReturn(playerModel);

        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setId(10L);
        playerEntity.setName("Brock Purdy");
        // playerEntity.getTeamEntity() is null by default

        when(playerRepository.findById(10L)).thenReturn(Optional.of(playerEntity));
        when(repository.save(any(TeamEntity.class))).thenReturn(teamEntity);
        when(mapper.domainToResponse(any(Team.class))).thenReturn(teamResponseDTO);

        // Act
        TeamResponseDTO result = teamService.createTeam(teamRequest);

        // Assert
        assertNotNull(result);
        assertEquals("San Francisco 49ers", result.getName());
        verify(repository).save(any(TeamEntity.class));
    }

    @Test
    void createTeam_PlayerAlreadyOnAnotherTeam_ThrowsException() {
        // Arrange
        when(repository.findByName("San Francisco 49ers")).thenReturn(Optional.empty());

        PlayerRequest pr = new PlayerRequest();
        pr.setName("Brock Purdy");
        pr.setTeam("SF");
        teamRequest.setRoster(List.of(pr));

        PlayerResponseDTO playerDTO = new PlayerResponseDTO();
        playerDTO.setId(10L);
        playerDTO.setName("Brock Purdy");

        Player playerModel = mock(Player.class);
        when(playerModel.getId()).thenReturn(10L);
        when(playerModel.getPosition()).thenReturn(com.jamalkarim.analyzer.domain.enums.Position.QB);

        when(playerService.getOrSyncPlayer("Brock Purdy", "SF")).thenReturn(playerDTO);
        when(playerMapper.responseToDomain(playerDTO)).thenReturn(playerModel);

        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setId(10L);
        playerEntity.setName("Brock Purdy");

        TeamEntity otherTeam = new TeamEntity();
        otherTeam.setName("Other Team");
        playerEntity.setTeamEntity(otherTeam);

        when(playerRepository.findById(10L)).thenReturn(Optional.of(playerEntity));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> teamService.createTeam(teamRequest));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("already on team"));
    }
}
