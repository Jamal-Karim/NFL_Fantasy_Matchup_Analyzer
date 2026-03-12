package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.dto.mock.MockPlayerDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.StatsEntity;

public class PlayerMapper {

    public PlayerEntity mockToEntity(MockPlayerDTO mockPlayerDTO) {
        StatsMapper statsMapper = new StatsMapper();
        PlayerEntity player = new PlayerEntity();

        player.setName(mockPlayerDTO.getName());
        player.setNFLTeam(mockPlayerDTO.getNflTeam());
        player.setPosition(mockPlayerDTO.getPosition());

        StatsEntity stats = statsMapper.mockToEntity(mockPlayerDTO.getStats());
        player.setStats(stats);
        return player;
    }
}
