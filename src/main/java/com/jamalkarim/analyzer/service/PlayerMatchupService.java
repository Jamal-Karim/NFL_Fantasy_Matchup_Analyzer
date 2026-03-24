package com.jamalkarim.analyzer.service;


import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupAnalyzer;
import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResultFactory;
import com.jamalkarim.analyzer.dto.response.PlayerMatchupResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import com.jamalkarim.analyzer.exceptions.MatchupNotFoundException;
import com.jamalkarim.analyzer.repository.PlayerMatchupRepository;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.utils.PlayerMatchupMapper;
import org.springframework.stereotype.Service;

/**
 * Service for analyzing and retrieving head-to-head player matchups.
 * Orchestrates the comparison of players and handles the persistence of matchup results.
 */
@Service
public class PlayerMatchupService {

    private final PlayerRepository playerRepository;
    private final PlayerMatchupRepository matchupRepository;
    private final PlayerMatchupMapper mapper;
    private final PlayerMatchupAnalyzer analyzer;
    private final ScareResultFactory factory;

    /**
     * Constructs a PlayerMatchupService with required dependencies.
     *
     * @param playerRepository  The repository for player data
     * @param matchupRepository The repository for matchup results
     * @param mapper            The mapper for converting between domain and entity/DTO models
     * @param analyzer          The component for analyzing player matchups
     * @param factory           The factory for generating Scare Factor results
     */
    public PlayerMatchupService(PlayerRepository playerRepository, PlayerMatchupRepository matchupRepository, PlayerMatchupMapper mapper, PlayerMatchupAnalyzer analyzer, ScareResultFactory factory) {
        this.playerRepository = playerRepository;
        this.matchupRepository = matchupRepository;
        this.mapper = mapper;
        this.analyzer = analyzer;
        this.factory = factory;
    }

    /**
     * Retrieves a stored player matchup report by its ID.
     * Re-generates Scare Factors for both players to ensure the response includes current analysis.
     *
     * @param id The ID of the matchup record
     * @return A DTO containing the matchup result and player analyses
     * @throws MatchupNotFoundException if the matchup record is not found
     */
    public PlayerMatchupResponseDTO getPlayerMatchupResponseById(long id) {

        PlayerMatchupResultEntity entity = matchupRepository.findById(id)
                .orElseThrow(() -> new MatchupNotFoundException("Player matchup with id " + id + " does not exist"));

        PlayerMatchupResult matchup = mapper.entityToDomain(entity);

        matchup.setPlayer1ScareResult(factory.generateScareResult(matchup.getPlayer1()));
        matchup.setPlayer2ScareResult(factory.generateScareResult(matchup.getPlayer2()));

        return mapper.domainToResponse(matchup);
    }

    /**
     * Analyzes a head-to-head matchup between two players and saves the result to the database.
     *
     * @param player1 The first player model
     * @param player2 The second player model
     * @return A DTO containing the results of the analysis
     */
    public PlayerMatchupResponseDTO createPlayerMatchup(Player player1, Player player2) {
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
