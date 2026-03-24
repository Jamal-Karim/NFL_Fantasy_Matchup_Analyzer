package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResultFactory;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.exceptions.PlayerNotFoundException;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import com.jamalkarim.analyzer.utils.ScareResultMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for managing Scare Factor analysis reports.
 * Provides functionality to generate and retrieve detailed performance threat assessments for players.
 */
@Service
public class ScareResultService {
    private final ScareResultFactory factory;
    private final PlayerRepository repository;
    private final PlayerMapper playerMapper;
    private final ScareResultMapper scareResultMapper;


    /**
     * Constructs a ScareResultService with required dependencies.
     *
     * @param factory           The factory for generating Scare Factor results
     * @param repository        The repository for player data
     * @param playerMapper      The mapper for player models
     * @param scareResultMapper The mapper for Scare Factor results
     */
    public ScareResultService(ScareResultFactory factory, PlayerRepository repository, PlayerMapper playerMapper, ScareResultMapper scareResultMapper) {
        this.factory = factory;
        this.repository = repository;
        this.playerMapper = playerMapper;
        this.scareResultMapper = scareResultMapper;
    }

    /**
     * Retrieves a detailed Scare Factor analysis for a specific player by their ID.
     *
     * @param playerId The database ID of the player
     * @return A DTO containing the scare score, tier, and textual explanations
     * @throws PlayerNotFoundException if the player record does not exist
     */
    public ScareResponseDTO getScareResultById(Long playerId) {
        Optional<PlayerEntity> entityOpt = repository.findById(playerId);
        if (entityOpt.isPresent()) {
            Player player = playerMapper.entityToDomain(entityOpt.get());
            return scareResultMapper.domainToResponse(factory.generateScareResult(player));
        } else {
            throw new PlayerNotFoundException();
        }
    }
}