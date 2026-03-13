package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerMatchupMapper {

    public PlayerMatchupResultEntity domainToEntity(PlayerMatchupResult playerMatchupResult) {
        PlayerMatchupResultEntity entity = new PlayerMatchupResultEntity();

        entity.setScareDifference(playerMatchupResult.getScareDifference());
        entity.setAdvantage(playerMatchupResult.getAdvantage());
        entity.setExplanation(playerMatchupResult.getExplanation());

        return entity;
    }
}
