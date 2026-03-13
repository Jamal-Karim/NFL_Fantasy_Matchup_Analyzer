package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupAnalyzer;
import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerMatchupMapper {

    private final PlayerMapper playerMapper;
    private final PlayerMatchupAnalyzer analyzer;

    public PlayerMatchupMapper(PlayerMapper playerMapper, PlayerMatchupAnalyzer analyzer) {
        this.playerMapper = playerMapper;
        this.analyzer = analyzer;
    }

    public PlayerMatchupResultEntity domainToEntity(PlayerMatchupResult playerMatchupResult) {
        PlayerMatchupResultEntity entity = new PlayerMatchupResultEntity();

        entity.setScareDifference(playerMatchupResult.getScareDifference());
        entity.setAdvantage(playerMatchupResult.getAdvantage());

        return entity;
    }

    public PlayerMatchupResult entityToDomain(PlayerMatchupResultEntity entity) {

        Player player1 = playerMapper.entityToDomain(entity.getPlayer1());
        Player player2 = playerMapper.entityToDomain(entity.getPlayer2());

        PlayerMatchupResult result = analyzer.analyzePlayerMatchup(player1, player2);

        result.setScareDifference(entity.getScareDifference());
        result.setAdvantage(entity.getAdvantage());

        if (entity.getWinner() != null) {
            result.setWinner(playerMapper.entityToDomain(entity.getWinner()));
        }
        if (entity.getLoser() != null) {
            result.setLoser(playerMapper.entityToDomain(entity.getLoser()));
        }

        ScareResult player1ScareResult = playerMapper.scareEntityToScareDomain(entity.getPlayer1ScareResult());
        ScareResult player2ScareResult = playerMapper.scareEntityToScareDomain(entity.getPlayer2ScareResult());

        result.setPlayer1ScareResult(player1ScareResult);
        result.setPlayer2ScareResult(player2ScareResult);

        return result;
    }
}
