package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.matchups.TeamMatchupAnalyzer;
import com.jamalkarim.analyzer.domain.matchups.TeamMatchupResult;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.models.QuarterBack;
import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.response.TeamMatchupResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import com.jamalkarim.analyzer.entities.TeamMatchupResultEntity;
import com.jamalkarim.analyzer.repository.PlayerMatchupRepository;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.repository.TeamMatchupRepository;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import com.jamalkarim.analyzer.utils.TeamMatchupMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamMatchupServiceTest {

    @Mock
    private TeamMatchupRepository teamMatchupRepository;
    @Mock
    private PlayerMatchupRepository playerMatchupRepository;
    @Mock
    private TeamMatchupAnalyzer teamMatchupAnalyzer;
    @Mock
    private TeamMatchupMapper teamMatchupMapper;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private PlayerMatchupService playerMatchupService;
    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private TeamMatchupService teamMatchupService;

    private Team team1;
    private Team team2;

    @BeforeEach
    void setUp() {
        team1 = new Team("Team 1");
        team2 = new Team("Team 2");
    }

    @Test
    void getTeamMatchupById_Success() {
        TeamMatchupResultEntity entity = new TeamMatchupResultEntity();
        TeamMatchupResult domain = new TeamMatchupResult("Team 1", "Team 2");
        TeamMatchupResponseDTO responseDTO = new TeamMatchupResponseDTO();

        when(teamMatchupRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(teamMatchupMapper.entityToDomain(entity)).thenReturn(domain);
        when(teamMatchupMapper.domainToResponse(domain)).thenReturn(responseDTO);

        TeamMatchupResponseDTO result = teamMatchupService.getTeamMatchupById(1L);

        assertNotNull(result);
        verify(teamMatchupRepository).findById(1L);
    }

    @Test
    void getTeamMatchupById_NotFound() {
        when(teamMatchupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teamMatchupService.getTeamMatchupById(1L));
    }

    @Test
    void createTeamMatchup_Existing() {
        TeamMatchupResultEntity entity = new TeamMatchupResultEntity();
        TeamMatchupResult domain = new TeamMatchupResult("Team 1", "Team 2");
        TeamMatchupResponseDTO responseDTO = new TeamMatchupResponseDTO();

        when(teamMatchupRepository.findByTeam1AndTeam2("Team 1", "Team 2")).thenReturn(Optional.of(entity));
        when(teamMatchupMapper.entityToDomain(entity)).thenReturn(domain);
        when(teamMatchupMapper.domainToResponse(domain)).thenReturn(responseDTO);

        TeamMatchupResponseDTO result = teamMatchupService.createTeamMatchup(team1, team2);

        assertNotNull(result);
        verify(teamMatchupRepository, never()).save(any());
    }

    @Test
    void createTeamMatchup_New() {
        when(teamMatchupRepository.findByTeam1AndTeam2("Team 1", "Team 2")).thenReturn(Optional.empty());

        TeamMatchupResult result = new TeamMatchupResult("Team 1", "Team 2");
        Player p1 = new QuarterBack("P1", "T1");
        p1.setTeam("T1");
        Player p2 = new QuarterBack("P2", "T2");
        p2.setTeam("T2");
        PlayerMatchupResult pmr = new PlayerMatchupResult(p1, p2);
        result.setPlayerMatchupResults(Collections.singletonList(pmr));

        TeamMatchupResultEntity entity = new TeamMatchupResultEntity();
        PlayerMatchupResultEntity pmrEntity = new PlayerMatchupResultEntity();
        entity.setPlayerMatchupResults(new ArrayList<>(Collections.singletonList(pmrEntity)));

        when(teamMatchupAnalyzer.analyzeTeamMatchup(team1, team2)).thenReturn(result);
        when(teamMatchupMapper.domainToEntity(result)).thenReturn(entity);
        when(teamMatchupRepository.save(entity)).thenReturn(entity);
        when(teamMatchupMapper.entityToDomain(entity)).thenReturn(result);
        when(teamMatchupMapper.domainToResponse(result)).thenReturn(new TeamMatchupResponseDTO());

        // For attachPlayerToMatchup
        when(playerRepository.findByNameAndNflTeam("P1", "T1")).thenReturn(Optional.of(new PlayerEntity()));
        when(playerRepository.findByNameAndNflTeam("P2", "T2")).thenReturn(Optional.of(new PlayerEntity()));

        TeamMatchupResponseDTO finalResponse = teamMatchupService.createTeamMatchup(team1, team2);

        assertNotNull(finalResponse);
        verify(teamMatchupRepository).save(entity);
    }
}
