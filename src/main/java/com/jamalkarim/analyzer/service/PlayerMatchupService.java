package com.jamalkarim.analyzer.service;


import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupAnalyzer;
import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResultFactory;
import com.jamalkarim.analyzer.dto.response.PlayerMatchupResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import com.jamalkarim.analyzer.repository.PlayerMatchupRepository;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.utils.PlayerMatchupMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerMatchupService {

    private final PlayerRepository playerRepository;
    private final PlayerMatchupRepository matchupRepository;
    private final PlayerMatchupMapper mapper;
    private final PlayerMatchupAnalyzer analyzer;
    private final ScareResultFactory factory;

    public PlayerMatchupService(PlayerRepository playerRepository, PlayerMatchupRepository matchupRepository, PlayerMatchupMapper mapper, PlayerMatchupAnalyzer analyzer, ScareResultFactory factory) {
        this.playerRepository = playerRepository;
        this.matchupRepository = matchupRepository;
        this.mapper = mapper;
        this.analyzer = analyzer;
        this.factory = factory;
    }

    public PlayerMatchupResponseDTO getPlayerMatchupResponseById(long id) {

        PlayerMatchupResultEntity entity = matchupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player matchup does not exist"));

        PlayerMatchupResult matchup = mapper.entityToDomain(entity);

        matchup.setPlayer1ScareResult(factory.generateScareResult(matchup.getPlayer1()));
        matchup.setPlayer2ScareResult(factory.generateScareResult(matchup.getPlayer2()));

        return mapper.domainToResponse(matchup);
    }

    public PlayerMatchupResponseDTO getPlayerMatchup(Player player1, Player player2) {
        PlayerMatchupResult result = analyzer.analyzePlayerMatchup(player1, player2);

        PlayerMatchupResultEntity playerMatchupResultEntity = mapper.domainToEntity(result);

        playerRepository.findByNameAndNflTeam(player1.getName(), player1.getTeam())
                .ifPresent(p1 -> {
                    playerMatchupResultEntity.setPlayer1(p1);
                    playerMatchupResultEntity.setPlayer1ScareResult(p1.getScareResult());

                    if (result.getWinner().isPresent() && result.getWinner().get().equals(player1)) {
                        playerMatchupResultEntity.setWinner(p1);
                    } else if (result.getLoser().isPresent() && result.getLoser().get().equals(player1)) {
                        playerMatchupResultEntity.setLoser(p1);
                    }
                });

        playerRepository.findByNameAndNflTeam(player2.getName(), player2.getTeam())
                .ifPresent(p2 -> {
                    playerMatchupResultEntity.setPlayer2(p2);
                    playerMatchupResultEntity.setPlayer2ScareResult(p2.getScareResult());

                    if (result.getWinner().isPresent() && result.getWinner().get().equals(player2)) {
                        playerMatchupResultEntity.setWinner(p2);
                    } else if (result.getLoser().isPresent() && result.getLoser().get().equals(player2)) {
                        playerMatchupResultEntity.setLoser(p2);
                    }
                });

        PlayerMatchupResultEntity savedEntity = matchupRepository.save(playerMatchupResultEntity);
        result.setId(savedEntity.getId());

        return mapper.domainToResponse(result);
    }
}
