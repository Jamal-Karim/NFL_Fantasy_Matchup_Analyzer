package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.Stats;
import com.jamalkarim.analyzer.dto.mock.MockStatsDTO;
import com.jamalkarim.analyzer.entities.StatsEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatsMapperTest {

    private StatsMapper statsMapper;

    @BeforeEach
    void setUp() {
        statsMapper = new StatsMapper();
    }

    @Test
    void entityToDomain_Success() {
        StatsEntity entity = new StatsEntity();
        entity.setSeason(2023);
        entity.setGamesPlayed(16);
        entity.setPassingYards(4000);

        Stats domain = statsMapper.entityToDomain(entity);

        assertEquals(2023, domain.getSeason());
        assertEquals(16, domain.getGamesPlayed());
        assertEquals(4000, domain.getPassingYards());
    }

    @Test
    void domainToMock_QB_IncludesPassingStats() {
        Stats domain = new Stats();
        domain.setSeason(2023);
        domain.setPassingYards(4000);
        domain.setReceivingYards(10);

        MockStatsDTO mock = statsMapper.domainToMock(domain, Position.QB);

        assertEquals(4000, mock.getPassingYards());
        assertNull(mock.getReceivingYards()); // Should be filtered out for QB in domainToMock
    }

    @Test
    void domainToMock_WR_IncludesReceivingStats() {
        Stats domain = new Stats();
        domain.setSeason(2023);
        domain.setPassingYards(4000);
        domain.setReceivingYards(1200);

        MockStatsDTO mock = statsMapper.domainToMock(domain, Position.WR);

        assertNull(mock.getPassingYards()); // Should be filtered out for WR
        assertEquals(1200, mock.getReceivingYards());
    }
}
