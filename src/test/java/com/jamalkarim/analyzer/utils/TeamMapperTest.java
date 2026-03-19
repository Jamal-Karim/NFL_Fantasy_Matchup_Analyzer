package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.models.QuarterBack;
import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.response.RosterMemberDTO;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.TeamEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamMapperTest {

    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private TeamMapper teamMapper;

    private TeamEntity teamEntity;
    private PlayerEntity playerEntity;

    @BeforeEach
    void setUp() {
        playerEntity = new PlayerEntity();
        playerEntity.setName("Player 1");

        teamEntity = new TeamEntity();
        teamEntity.setId(1L);
        teamEntity.setName("Test Team");
        teamEntity.setRoster(Collections.singletonList(playerEntity));
    }

    @Test
    void entityToDomain_Success() {
        Player playerDomain = new QuarterBack("Player 1", "T1");
        when(playerMapper.entityToDomain(playerEntity)).thenReturn(playerDomain);

        Team domain = teamMapper.entityToDomain(teamEntity);

        assertEquals(1L, domain.getId());
        assertEquals("Test Team", domain.getName());
        assertEquals(1, domain.getRoster().size());
        assertEquals("Player 1", domain.getRoster().get(0).getName());
    }

    @Test
    void domainToResponse_Success() {
        Team team = new Team("Test Team");
        team.setId(1L);
        Player player = new QuarterBack("Player 1", "T1");
        team.setRoster(Collections.singletonList(player));

        RosterMemberDTO rosterMemberDTO = new RosterMemberDTO();
        rosterMemberDTO.setName("Player 1");

        when(playerMapper.domainToRosterMember(player)).thenReturn(rosterMemberDTO);

        TeamResponseDTO response = teamMapper.domainToResponse(team);

        assertEquals(1L, response.getId());
        assertEquals("Test Team", response.getName());
        assertEquals(1, response.getRoster().size());
        assertEquals("Player 1", response.getRoster().get(0).getName());
    }
}
