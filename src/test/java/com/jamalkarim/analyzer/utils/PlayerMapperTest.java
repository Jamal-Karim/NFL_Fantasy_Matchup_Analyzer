package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.models.QuarterBack;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.RosterMemberDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMapperTest {

    private PlayerMapper playerMapper;

    @BeforeEach
    void setUp() {
        playerMapper = new PlayerMapper();
    }

    @Test
    void entityToDomain_Success() {
        PlayerEntity entity = new PlayerEntity();
        entity.setName("Patrick Mahomes");
        entity.setNflTeam("KC");
        entity.setPosition(Position.QB);
        entity.setId(1L);

        Player domain = playerMapper.entityToDomain(entity);

        assertTrue(domain instanceof QuarterBack);
        assertEquals("Patrick Mahomes", domain.getName());
        assertEquals("KC", domain.getTeam());
        assertEquals(1L, domain.getId());
    }

    @Test
    void domainToResponse_Success() {
        Player player = new QuarterBack("Patrick Mahomes", "KC");
        player.setId(1L);

        PlayerResponseDTO response = playerMapper.domainToResponse(player);

        assertEquals(1L, response.getId());
        assertEquals("Patrick Mahomes", response.getName());
        assertEquals("KC", response.getNflTeam());
        assertEquals(Position.QB, response.getPosition());
    }

    @Test
    void domainToRosterMember_Success() {
        Player player = new QuarterBack("Patrick Mahomes", "KC");
        player.setId(1L);

        RosterMemberDTO rosterMember = playerMapper.domainToRosterMember(player);

        assertEquals(1L, rosterMember.getId());
        assertEquals("Patrick Mahomes", rosterMember.getName());
        assertEquals("KC", rosterMember.getNflTeam());
        assertEquals(Position.QB, rosterMember.getPosition());
    }
}
