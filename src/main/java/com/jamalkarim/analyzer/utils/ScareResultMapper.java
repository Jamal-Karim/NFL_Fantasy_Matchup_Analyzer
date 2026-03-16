package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.entities.ScareResultEntity;
import org.springframework.stereotype.Component;

@Component
public class ScareResultMapper {

    private final PlayerMapper mapper = new PlayerMapper();

    public ScareResultEntity scareDomainToScareEntity(ScareResult scareResult) {
        ScareResultEntity scareResultEntity = new ScareResultEntity();
        scareResultEntity.setScareScore(scareResult.getScareScore());
        scareResultEntity.setPlayerTier(scareResult.getScareTier());
        return scareResultEntity;
    }

    public ScareResult scareEntityToScareDomain(ScareResultEntity entity) {
        ScareResult result = new ScareResult(mapper.entityToDomain(entity.getPlayer()));

        result.setScareScore(entity.getScareScore());
        result.setScareTier(entity.getPlayerTier());
        result.setPrimaryExplanation(entity.getPrimaryExplanation());
        result.setSupportingExplanations(entity.getSupportingExplanations());

        return result;
    }

    public ScareResponseDTO domainToResponse(ScareResult scareResult) {
        ScareResponseDTO scareResponseDTO = new ScareResponseDTO();
        scareResponseDTO.setName(scareResult.getName());
        scareResponseDTO.setTeam(scareResult.getTeam());
        scareResponseDTO.setPosition(scareResult.getPosition());
        scareResponseDTO.setScareScore(scareResult.getScareScore());
        scareResponseDTO.setScareTier(scareResult.getScareTier());
        scareResponseDTO.setPrimaryExplanation(scareResult.getPrimaryExplanation());
        scareResponseDTO.setSupportingExplanations(scareResult.getSupportingExplanations());
        return scareResponseDTO;
    }
}
