package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.entities.ScareResultEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper utility for Scare Factor analysis results.
 * Handles conversions between ScareResult domain models, JPA entities, and API response DTOs.
 */
@Component
public class ScareResultMapper {

    private final PlayerMapper mapper = new PlayerMapper();

    /**
     * Converts a ScareResult domain model to its database entity representation.
     *
     * @param scareResult The domain model
     * @return A database entity
     */
    public ScareResultEntity scareDomainToScareEntity(ScareResult scareResult) {
        ScareResultEntity scareResultEntity = new ScareResultEntity();
        scareResultEntity.setScareScore(scareResult.getScareScore());
        scareResultEntity.setPlayerTier(scareResult.getScareTier());
        return scareResultEntity;
    }

    /**
     * Converts a ScareResult database entity to its domain model.
     *
     * @param entity The stored entity
     * @return A domain model
     */
    public ScareResult scareEntityToScareDomain(ScareResultEntity entity) {
        ScareResult result = new ScareResult(mapper.entityToDomain(entity.getPlayer()));

        result.setScareScore(entity.getScareScore());
        result.setScareTier(entity.getPlayerTier());
        result.setPrimaryExplanation(entity.getPrimaryExplanation());
        result.setSupportingExplanations(entity.getSupportingExplanations());

        return result;
    }

    /**
     * Converts a ScareResult domain model to an API response representation.
     *
     * @param scareResult The domain model
     * @return An API response DTO
     */
    public ScareResponseDTO domainToResponse(ScareResult scareResult) {
        ScareResponseDTO scareResponseDTO = new ScareResponseDTO();
        scareResponseDTO.setName(scareResult.getName());
        scareResponseDTO.setTeam(scareResult.getTeam());
        scareResponseDTO.setPosition(scareResult.getPosition());
        scareResponseDTO.setScareScore(NumberUtils.round(scareResult.getScareScore()));
        scareResponseDTO.setScareTier(scareResult.getScareTier());
        scareResponseDTO.setPrimaryExplanation(scareResult.getPrimaryExplanation());
        scareResponseDTO.setSupportingExplanations(scareResult.getSupportingExplanations());
        return scareResponseDTO;
    }
}
