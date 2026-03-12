package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.models.*;
import com.jamalkarim.analyzer.dto.mock.MockPlayerDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.StatsEntity;

public class PlayerMapper {

    private final StatsMapper statsMapper = new StatsMapper();

    public Player entityToDomain(PlayerEntity playerEntity) {
        Position position = playerEntity.getPosition();

        Player domainPlayer = switch (position) {
            case QB -> new QuarterBack(playerEntity.getName(), playerEntity.getNFLTeam());
            case RB -> new RunningBack(playerEntity.getName(), playerEntity.getNFLTeam());
            case WR -> new WideReceiver(playerEntity.getName(), playerEntity.getNFLTeam());
            case TE -> new TightEnd(playerEntity.getName(), playerEntity.getNFLTeam());
        };

        domainPlayer.setDraftPick(playerEntity.getDraftPick());
        domainPlayer.setRookie(playerEntity.isRookie());
        domainPlayer.setInjured(playerEntity.isInjured());

        if (playerEntity.getCurrentSeasonStats() != null) {
            domainPlayer.setCurrentSeasonStats(statsMapper.entityToDomain(playerEntity.getCurrentSeasonStats()));
        }

        if (playerEntity.getLastSeasonStats() != null) {
            domainPlayer.setLastSeasonStats(statsMapper.entityToDomain(playerEntity.getLastSeasonStats()));
        }

        return domainPlayer;
    }

    public PlayerEntity mockToEntity(MockPlayerDTO mockPlayerDTO) {
        PlayerEntity player = new PlayerEntity();

        player.setName(mockPlayerDTO.getName());
        player.setNFLTeam(mockPlayerDTO.getNflTeam());
        player.setPosition(mockPlayerDTO.getPosition());

        StatsEntity currentStats = statsMapper.mockToEntity(mockPlayerDTO.getCurrentSeasonStats());
        StatsEntity lastStats = statsMapper.mockToEntity(mockPlayerDTO.getLastSeasonStats());
        player.setCurrentSeasonStats(currentStats);
        player.setLastSeasonStats(lastStats);

        player.setDraftPick(mockPlayerDTO.getDraftPick());
        player.setRookie(mockPlayerDTO.isRookie());
        player.setInjured(mockPlayerDTO.isInjured());

        return player;
    }
}
