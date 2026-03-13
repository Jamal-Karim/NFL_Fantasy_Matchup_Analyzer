package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.Stats;
import com.jamalkarim.analyzer.dto.mock.MockStatsDTO;
import com.jamalkarim.analyzer.entities.StatsEntity;

public class StatsMapper {

    private int zeroIfNull(Integer value) {
        return value == null ? 0 : value;
    }

    public Stats entityToDomain(StatsEntity statsEntity) {
        Stats stats = new Stats();

        stats.setSeason(statsEntity.getSeason());
        stats.setGamesPlayed(statsEntity.getGamesPlayed());

        stats.setPassAttempts(statsEntity.getPassAttempts());
        stats.setCompletions(statsEntity.getCompletions());
        stats.setPassingYards(statsEntity.getPassingYards());
        stats.setPassingTDs(statsEntity.getPassingTDs());
        stats.setInterceptions(statsEntity.getInterceptions());

        stats.setRushingAttempts(statsEntity.getRushingAttempts());
        stats.setRushingYards(statsEntity.getRushingYards());
        stats.setRushingTDs(statsEntity.getRushingTDs());

        stats.setReceptions(statsEntity.getReceptions());
        stats.setReceivingYards(statsEntity.getReceivingYards());
        stats.setReceivingTDs(statsEntity.getReceivingTDs());

        return stats;
    }

    public StatsEntity domainToEntity(Stats domainStats) {

        if (domainStats == null) {
            return null;
        }

        StatsEntity stats = new StatsEntity();

        stats.setSeason(domainStats.getSeason());
        stats.setGamesPlayed(domainStats.getGamesPlayed());

        stats.setPassAttempts(domainStats.getPassAttempts());
        stats.setCompletions(domainStats.getCompletions());
        stats.setPassingYards(domainStats.getPassingYards());
        stats.setPassingTDs(domainStats.getPassingTDs());
        stats.setInterceptions(domainStats.getInterceptions());

        stats.setRushingAttempts(domainStats.getRushingAttempts());
        stats.setRushingYards(domainStats.getRushingYards());
        stats.setRushingTDs(domainStats.getRushingTDs());

        stats.setReceptions(domainStats.getReceptions());
        stats.setReceivingYards(domainStats.getReceivingYards());
        stats.setReceivingTDs(domainStats.getReceivingTDs());

        return stats;
    }

    public StatsEntity mockToEntity(MockStatsDTO mockStatsDTO) {

        if (mockStatsDTO == null) {
            return null;
        }

        StatsEntity stats = new StatsEntity();

        stats.setSeason(zeroIfNull(mockStatsDTO.getSeason()));
        stats.setGamesPlayed(zeroIfNull(mockStatsDTO.getGamesPlayed()));

        stats.setPassAttempts(zeroIfNull(mockStatsDTO.getPassAttempts()));
        stats.setCompletions(zeroIfNull(mockStatsDTO.getCompletions()));
        stats.setPassingYards(zeroIfNull(mockStatsDTO.getPassingYards()));
        stats.setPassingTDs(zeroIfNull(mockStatsDTO.getPassingTDs()));
        stats.setInterceptions(zeroIfNull(mockStatsDTO.getInterceptions()));

        stats.setRushingAttempts(zeroIfNull(mockStatsDTO.getRushingAttempts()));
        stats.setRushingYards(zeroIfNull(mockStatsDTO.getRushingYards()));
        stats.setRushingTDs(zeroIfNull(mockStatsDTO.getRushingTDs()));

        stats.setReceptions(zeroIfNull(mockStatsDTO.getReceptions()));
        stats.setReceivingYards(zeroIfNull(mockStatsDTO.getReceivingYards()));
        stats.setReceivingTDs(zeroIfNull(mockStatsDTO.getReceivingTDs()));

        return stats;
    }

    public MockStatsDTO domainToMock(Stats domainStats, Position position) {

        if (domainStats == null) {
            return null;
        }

        MockStatsDTO stats = new MockStatsDTO();

        stats.setSeason(domainStats.getSeason());
        stats.setGamesPlayed(domainStats.getGamesPlayed());

        if (position == Position.QB) {
            stats.setPassAttempts(domainStats.getPassAttempts());
            stats.setCompletions(domainStats.getCompletions());
            stats.setPassingYards(domainStats.getPassingYards());
            stats.setPassingTDs(domainStats.getPassingTDs());
            stats.setInterceptions(domainStats.getInterceptions());
            stats.setRushingAttempts(domainStats.getRushingAttempts());
            stats.setRushingYards(domainStats.getRushingYards());
            stats.setRushingTDs(domainStats.getRushingTDs());
        }

        if (position == Position.RB) {
            stats.setRushingAttempts(domainStats.getRushingAttempts());
            stats.setRushingYards(domainStats.getRushingYards());
            stats.setRushingTDs(domainStats.getRushingTDs());

            stats.setReceptions(domainStats.getReceptions());
            stats.setReceivingYards(domainStats.getReceivingYards());
            stats.setReceivingTDs(domainStats.getReceivingTDs());
        }

        if (position == Position.WR || position == Position.TE) {
            stats.setReceptions(domainStats.getReceptions());
            stats.setReceivingYards(domainStats.getReceivingYards());
            stats.setReceivingTDs(domainStats.getReceivingTDs());

        }
        return stats;
    }
}