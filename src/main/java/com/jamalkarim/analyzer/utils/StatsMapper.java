package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.stats.Stats;
import com.jamalkarim.analyzer.dto.mock.MockStatsDTO;
import com.jamalkarim.analyzer.entities.StatsEntity;

public class StatsMapper {

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
        StatsEntity stats = new StatsEntity();

        stats.setSeason(mockStatsDTO.getSeason());
        stats.setGamesPlayed(mockStatsDTO.getGamesPlayed());

        stats.setPassAttempts(mockStatsDTO.getPassAttempts());
        stats.setCompletions(mockStatsDTO.getCompletions());
        stats.setPassingYards(mockStatsDTO.getPassingYards());
        stats.setPassingTDs(mockStatsDTO.getPassingTDs());
        stats.setInterceptions(mockStatsDTO.getInterceptions());

        stats.setRushingAttempts(mockStatsDTO.getRushingAttempts());
        stats.setRushingYards(mockStatsDTO.getRushingYards());
        stats.setRushingTDs(mockStatsDTO.getRushingTDs());

        stats.setReceptions(mockStatsDTO.getReceptions());
        stats.setReceivingYards(mockStatsDTO.getReceivingYards());
        stats.setReceivingTDs(mockStatsDTO.getReceivingTDs());

        return stats;
    }
}
