package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerMatchupMapper {

    private final PlayerMapper playerMapper = new PlayerMapper();

    public PlayerMatchupResultEntity domainToEntity(PlayerMatchupResult playerMatchupResult) {
        PlayerMatchupResultEntity entity = new PlayerMatchupResultEntity();

        entity.setScareDifference(playerMatchupResult.getScareDifference());
        entity.setAdvantage(playerMatchupResult.getAdvantage());
        entity.setExplanation(playerMatchupResult.getExplanation());

        return entity;
    }

    public PlayerMatchupResult entityToDomain(PlayerMatchupResultEntity entity) {

        Player player1 = playerMapper.entityToDomain(entity.getPlayer1());
        Player player2 = playerMapper.entityToDomain(entity.getPlayer2());

        PlayerMatchupResult result = new PlayerMatchupResult(player1, player2);

        result.setScareDifference(entity.getScareDifference());
        result.setAdvantage(entity.getAdvantage());
        result.setExplanation(entity.getExplanation());

        result.setWinner(playerMapper.entityToDomain(entity.getWinner()));
        result.setLoser(playerMapper.entityToDomain(entity.getLoser()));

        result.getWinner();

        ScareResult player1ScareResult = playerMapper.scareEntityToScareDomain(entity.getPlayer1ScareResult());
        ScareResult player2ScareResult = playerMapper.scareEntityToScareDomain(entity.getPlayer2ScareResult());

        result.setPlayer1ScareResult(player1ScareResult);
        result.setPlayer2ScareResult(player2ScareResult);

        return result;
    }
}
