package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.dto.mock.MockStatsDTO;
import com.jamalkarim.analyzer.entities.StatsEntity;

public class StatsMapper {

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
