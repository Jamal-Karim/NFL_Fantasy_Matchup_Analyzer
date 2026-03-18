package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.matchups.TeamMatchupResult;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import com.jamalkarim.analyzer.entities.TeamMatchupResultEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TeamMatchupMapper {

    private final PlayerMatchupMapper playerMatchupMapper;

    public TeamMatchupMapper(PlayerMatchupMapper playerMatchupMapper) {
        this.playerMatchupMapper = playerMatchupMapper;
    }

    public TeamMatchupResultEntity domainToEntity(TeamMatchupResult domain) {
        TeamMatchupResultEntity entity = new TeamMatchupResultEntity();

        entity.setTeam1(domain.getTeam1());
        entity.setTeam2(domain.getTeam2());

        entity.setTeam1TotalScore(domain.getTeam1TotalScore());
        entity.setTeam2TotalScore(domain.getTeam2TotalScore());

        entity.setTeam1Probability(domain.getTeam1Probability());
        entity.setTeam2Probability(domain.getTeam2Probability());
        entity.setAdvantage(domain.getAdvantage());

        List<PlayerMatchupResultEntity> playerMatchupResultEntities = new ArrayList<>();

        for (PlayerMatchupResult matchupResult : domain.getPlayerMatchupResults()) {
            playerMatchupResultEntities.add(playerMatchupMapper.domainToEntity(matchupResult));
        }

        entity.setPlayerMatchupResults(playerMatchupResultEntities);
        return entity;
    }

    public TeamMatchupResult entityToDomain(TeamMatchupResultEntity entity) {
        TeamMatchupResult result = new TeamMatchupResult(entity.getTeam1(), entity.getTeam2());

        result.setTeam1TotalScore(entity.getTeam1TotalScore());
        result.setTeam2TotalScore(entity.getTeam2TotalScore());

        result.setTeam1Probability(entity.getTeam1Probability());
        result.setTeam2Probability(entity.getTeam2Probability());
        result.setAdvantage(entity.getAdvantage());

        List<PlayerMatchupResult> playerMatchupResults = new ArrayList<>();

        for (PlayerMatchupResultEntity playerMatchupResultEntity : entity.getPlayerMatchupResults()) {
            playerMatchupResults.add(playerMatchupMapper.entityToDomain(playerMatchupResultEntity));
        }

        result.setPlayerMatchupResults(playerMatchupResults);
        result.setId(entity.getId());
        return result;
    }
}
