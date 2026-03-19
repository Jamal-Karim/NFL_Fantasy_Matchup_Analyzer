package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.matchups.TeamMatchupResult;
import com.jamalkarim.analyzer.dto.response.PlayerMatchupResponseForTeamDTO;
import com.jamalkarim.analyzer.dto.response.TeamMatchupResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import com.jamalkarim.analyzer.entities.TeamMatchupResultEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamMatchupMapperTest {

    @Mock
    private PlayerMatchupMapper playerMatchupMapper;

    @InjectMocks
    private TeamMatchupMapper teamMatchupMapper;

    private TeamMatchupResult domain;
    private TeamMatchupResultEntity entity;

    @BeforeEach
    void setUp() {
        domain = new TeamMatchupResult("Team 1", "Team 2");
        domain.setTeam1TotalScore(100.0);
        domain.setTeam2TotalScore(90.0);
        domain.setTeam1Probability(0.6);
        domain.setTeam2Probability(0.4);

        entity = new TeamMatchupResultEntity();
        entity.setId(1L);
        entity.setTeam1("Team 1");
        entity.setTeam2("Team 2");
        entity.setTeam1TotalScore(100.0);
        entity.setTeam2TotalScore(90.0);
        entity.setTeam1Probability(0.6);
        entity.setTeam2Probability(0.4);
        entity.setPlayerMatchupResults(Collections.emptyList());
    }

    @Test
    void domainToEntity_Success() {
        domain.setPlayerMatchupResults(Collections.emptyList());
        TeamMatchupResultEntity result = teamMatchupMapper.domainToEntity(domain);

        assertEquals("Team 1", result.getTeam1());
        assertEquals(100.0, result.getTeam1TotalScore());
    }

    @Test
    void entityToDomain_Success() {
        TeamMatchupResult result = teamMatchupMapper.entityToDomain(entity);

        assertEquals("Team 1", result.getTeam1());
        assertEquals(100.0, result.getTeam1TotalScore());
    }

    @Test
    void domainToResponse_Success() {
        domain.setPlayerMatchupResults(Collections.emptyList());
        TeamMatchupResponseDTO response = teamMatchupMapper.domainToResponse(domain);

        assertEquals("Team 1", response.getTeam1());
        assertEquals(100.0, response.getTeam1TotalScore());
    }
}
