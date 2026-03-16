package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.domain.scoring.ScareResultFactory;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.exceptions.PlayerNotFoundException;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import com.jamalkarim.analyzer.utils.ScareResultMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScareResultService {
    private final ScareResultFactory factory;
    private final PlayerRepository repository;
    private final PlayerMapper playerMapper;
    private final ScareResultMapper scareResultMapper;


    public ScareResultService(ScareResultFactory factory, PlayerRepository repository, PlayerMapper playerMapper, ScareResultMapper scareResultMapper) {
        this.factory = factory;
        this.repository = repository;
        this.playerMapper = playerMapper;
        this.scareResultMapper = scareResultMapper;
    }

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