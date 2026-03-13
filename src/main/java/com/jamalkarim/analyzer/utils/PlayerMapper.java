package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.models.*;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.dto.mock.MockPlayerDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.ScareResultEntity;
import com.jamalkarim.analyzer.entities.StatsEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    private final StatsMapper statsMapper = new StatsMapper();

    public Player entityToDomain(PlayerEntity playerEntity) {
        Position position = playerEntity.getPosition();

        Player domainPlayer = switch (position) {
            case QB -> new QuarterBack(playerEntity.getName(), playerEntity.getNflTeam());
            case RB -> new RunningBack(playerEntity.getName(), playerEntity.getNflTeam());
            case WR -> new WideReceiver(playerEntity.getName(), playerEntity.getNflTeam());
            case TE -> new TightEnd(playerEntity.getName(), playerEntity.getNflTeam());
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
        player.setNflTeam(mockPlayerDTO.getNflTeam());
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

    public PlayerEntity domainToEntity(Player player) {
        PlayerEntity playerEntity = new PlayerEntity();

        playerEntity.setName(player.getName());
        playerEntity.setNflTeam(player.getTeam());
        playerEntity.setPosition(player.getPosition());

        StatsEntity currentStats = statsMapper.domainToEntity(player.getCurrentSeasonStats());
        StatsEntity lastStats = statsMapper.domainToEntity(player.getLastSeasonStats());
        playerEntity.setCurrentSeasonStats(currentStats);
        playerEntity.setLastSeasonStats(lastStats);

        playerEntity.setDraftPick(player.getDraftPick());
        playerEntity.setRookie(player.isRookie());
        playerEntity.setInjured(player.isInjured());

        return playerEntity;
    }

    public ScareResultEntity scareDomainToScareEntity(ScareResult scareResult) {
        ScareResultEntity scareResultEntity = new ScareResultEntity();
        scareResultEntity.setScareScore(scareResult.getScareScore());
        scareResultEntity.setPlayerTier(scareResult.getScareTier());
        return scareResultEntity;
    }

    public ScareResult scareEntityToScareDomain(ScareResultEntity entity) {
        ScareResult result = new ScareResult(entityToDomain(entity.getPlayer()));

        result.setScareScore(entity.getScareScore());
        result.setScareTier(entity.getPlayerTier());
        result.setPrimaryExplanation(entity.getPrimaryExplanation());
        result.setSupportingExplanations(entity.getSupportingExplanations());

        return result;
    }
}
