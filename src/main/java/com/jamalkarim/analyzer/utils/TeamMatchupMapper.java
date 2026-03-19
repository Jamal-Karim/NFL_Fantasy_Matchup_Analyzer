package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.matchups.TeamMatchupResult;
import com.jamalkarim.analyzer.dto.response.PlayerMatchupResponseForTeamDTO;
import com.jamalkarim.analyzer.dto.response.TeamMatchupResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import com.jamalkarim.analyzer.entities.TeamMatchupResultEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Mapper utility for team matchup data.
 * Handles conversions between TeamMatchupResult domain models, JPA entities, and API response DTOs.
 */
@Component
public class TeamMatchupMapper {

    private final PlayerMatchupMapper playerMatchupMapper;

    public TeamMatchupMapper(PlayerMatchupMapper playerMatchupMapper) {
        this.playerMatchupMapper = playerMatchupMapper;
    }

    /**
     * Converts a team matchup domain model to its database entity representation.
     *
     * @param domain The team matchup domain model
     * @return A database entity
     */
    public TeamMatchupResultEntity domainToEntity(TeamMatchupResult domain) {
        TeamMatchupResultEntity entity = new TeamMatchupResultEntity();

        entity.setTeam1(domain.getTeam1());
        entity.setTeam2(domain.getTeam2());

        entity.setTeam1TotalScore(domain.getTeam1TotalScore());
        entity.setTeam2TotalScore(domain.getTeam2TotalScore());

        entity.setTeam1Probability(domain.getTeam1Probability());
        entity.setTeam2Probability(domain.getTeam2Probability());
        entity.setAdvantage(domain.getAdvantage());

        List<PlayerMatchupResultEntity> playerMatchupResultEntities = new LinkedList<>();

        for (PlayerMatchupResult matchupResult : domain.getPlayerMatchupResults()) {
            playerMatchupResultEntities.add(playerMatchupMapper.domainToEntity(matchupResult));
        }

        entity.setPlayerMatchupResults(playerMatchupResultEntities);
        return entity;
    }

    /**
     * Converts a team matchup database entity to its domain model.
     *
     * @param entity The stored team matchup entity
     * @return A domain model
     */
    public TeamMatchupResult entityToDomain(TeamMatchupResultEntity entity) {
        TeamMatchupResult result = new TeamMatchupResult(entity.getTeam1(), entity.getTeam2());

        result.setTeam1TotalScore(entity.getTeam1TotalScore());
        result.setTeam2TotalScore(entity.getTeam2TotalScore());

        result.setTeam1Probability(entity.getTeam1Probability());
        result.setTeam2Probability(entity.getTeam2Probability());
        result.setAdvantage(entity.getAdvantage());

        List<PlayerMatchupResult> playerMatchupResults = new LinkedList<>();

        for (PlayerMatchupResultEntity playerMatchupResultEntity : entity.getPlayerMatchupResults()) {
            playerMatchupResults.add(playerMatchupMapper.entityToDomain(playerMatchupResultEntity));
        }

        result.setPlayerMatchupResults(playerMatchupResults);
        result.setId(entity.getId());
        return result;
    }

    /**
     * Converts a team matchup domain model to its API response representation.
     *
     * @param domain The team matchup domain model
     * @return An API response DTO
     */
    public TeamMatchupResponseDTO domainToResponse(TeamMatchupResult domain) {
        TeamMatchupResponseDTO responseDTO = new TeamMatchupResponseDTO();
        responseDTO.setId(domain.getId());
        responseDTO.setTeam1(domain.getTeam1());
        responseDTO.setTeam2(domain.getTeam2());
        responseDTO.setTeam1TotalScore(NumberUtils.round(domain.getTeam1TotalScore()));
        responseDTO.setTeam2TotalScore(NumberUtils.round(domain.getTeam2TotalScore()));
        responseDTO.setTeam1Probability(NumberUtils.round(domain.getTeam1Probability()));
        responseDTO.setTeam2Probability(NumberUtils.round(domain.getTeam2Probability()));
        responseDTO.setAdvantage(domain.getAdvantage());

        List<PlayerMatchupResponseForTeamDTO> playerMatchups = new LinkedList<>();

        for (PlayerMatchupResult domainMatchups : domain.getPlayerMatchupResults()) {
            playerMatchups.add(playerMatchupMapper.domainToTeamResponse(domainMatchups));
        }

        responseDTO.setPlayerMatchupResponses(playerMatchups);

        return responseDTO;
    }
}
