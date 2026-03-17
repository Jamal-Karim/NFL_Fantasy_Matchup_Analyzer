package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupAnalyzer;
import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.dto.response.PlayerMatchupResponseDTO;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper utility for head-to-head player matchup data.
 * Handles conversions between domain models, JPA entities, and API response DTOs.
 */
@Component
public class PlayerMatchupMapper {

    private final PlayerMapper playerMapper;
    private final ScareResultMapper scareResultMapper;
    private final PlayerMatchupAnalyzer analyzer;

    public PlayerMatchupMapper(PlayerMapper playerMapper, ScareResultMapper scareResultMapper, PlayerMatchupAnalyzer analyzer) {
        this.playerMapper = playerMapper;
        this.scareResultMapper = scareResultMapper;
        this.analyzer = analyzer;
    }

    /**
     * Converts a player matchup domain model to its database entity representation.
     *
     * @param playerMatchupResult The domain model
     * @return A database entity
     */
    public PlayerMatchupResultEntity domainToEntity(PlayerMatchupResult playerMatchupResult) {
        PlayerMatchupResultEntity entity = new PlayerMatchupResultEntity();

        entity.setScareDifference(playerMatchupResult.getScareDifference());
        entity.setAdvantage(playerMatchupResult.getAdvantage());

        return entity;
    }

    /**
     * Converts a player matchup database entity to its domain model.
     *
     * @param entity The stored matchup entity
     * @return A domain model
     */
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

        ScareResult player1ScareResult = scareResultMapper.scareEntityToScareDomain(entity.getPlayer1ScareResult());
        ScareResult player2ScareResult = scareResultMapper.scareEntityToScareDomain(entity.getPlayer2ScareResult());

        result.setPlayer1ScareResult(player1ScareResult);
        result.setPlayer2ScareResult(player2ScareResult);

        return result;
    }

    /**
     * Converts a player matchup domain model to its API response representation.
     *
     * @param playerMatchupResult The domain model
     * @return An API response DTO
     */
    public PlayerMatchupResponseDTO domainToResponse(PlayerMatchupResult playerMatchupResult) {
        PlayerMatchupResponseDTO playerMatchupResponseDTO = new PlayerMatchupResponseDTO();

        playerMatchupResponseDTO.setId(playerMatchupResult.getId());

        playerMatchupResponseDTO.setWinner(
                playerMatchupResult.getWinner()
                        .map(Player::getName)
                        .orElse("TIE")
        );

        playerMatchupResponseDTO.setLoser(
                playerMatchupResult.getLoser()
                        .map(Player::getName)
                        .orElse("TIE")
        );

        playerMatchupResponseDTO.setScareDifference(playerMatchupResult.getScareDifference());
        playerMatchupResponseDTO.setAdvantage(playerMatchupResult.getAdvantage());
        playerMatchupResponseDTO.setExplanation(playerMatchupResult.getExplanation());

        ScareResponseDTO player1ScareResult = scareResultMapper.domainToResponse(playerMatchupResult.getPlayer1ScareResult());
        playerMatchupResponseDTO.setPlayer1ScareResult(player1ScareResult);

        ScareResponseDTO player2ScareResult = scareResultMapper.domainToResponse(playerMatchupResult.getPlayer2ScareResult());
        playerMatchupResponseDTO.setPlayer2ScareResult(player2ScareResult);

        return playerMatchupResponseDTO;
    }
}
