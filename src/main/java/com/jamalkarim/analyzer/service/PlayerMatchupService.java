package com.jamalkarim.analyzer.service;


import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupAnalyzer;
import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import com.jamalkarim.analyzer.repository.PlayerMatchupRepository;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.utils.PlayerMatchupMapper;
import org.springframework.stereotype.Service;

@Service
public class PlayerMatchupService {

    private final PlayerRepository playerRepository;
    private final PlayerMatchupRepository matchupRepository;
    private final PlayerMatchupMapper mapper;

    public PlayerMatchupService(PlayerRepository playerRepository, PlayerMatchupRepository matchupRepository, PlayerMatchupMapper mapper) {
        this.playerRepository = playerRepository;
        this.matchupRepository = matchupRepository;
        this.mapper = mapper;
    }

    public PlayerMatchupResult getPlayerMatchup(Player player1, Player player2) {
        PlayerMatchupAnalyzer analyzer = new PlayerMatchupAnalyzer();
        PlayerMatchupResult result = analyzer.analyzePlayerMatchup(player1, player2);

        PlayerMatchupResultEntity playerMatchupResultEntity = mapper.domainToEntity(result);

        playerRepository.findByNameAndNflTeam(player1.getName(), player1.getTeam())
                .ifPresent(p1 -> {
                    playerMatchupResultEntity.setPlayer1(p1);
                    playerMatchupResultEntity.setPlayer1ScareResult(p1.getScareResult());
                });

        playerRepository.findByNameAndNflTeam(player2.getName(), player2.getTeam())
                .ifPresent(p2 -> {
                    playerMatchupResultEntity.setPlayer2(p2);
                    playerMatchupResultEntity.setPlayer2ScareResult(p2.getScareResult()); // Fixed the P1 overwrite
                });

        matchupRepository.save(playerMatchupResultEntity);
        return result;
    }
}
