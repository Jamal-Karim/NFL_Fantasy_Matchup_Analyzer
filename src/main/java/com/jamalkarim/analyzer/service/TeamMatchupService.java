package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.matchups.TeamMatchupAnalyzer;
import com.jamalkarim.analyzer.domain.matchups.TeamMatchupResult;
import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.response.TeamMatchupResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import com.jamalkarim.analyzer.entities.TeamMatchupResultEntity;
import com.jamalkarim.analyzer.exceptions.MatchupNotFoundException;
import com.jamalkarim.analyzer.repository.PlayerMatchupRepository;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.repository.TeamMatchupRepository;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import com.jamalkarim.analyzer.utils.TeamMatchupMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for analyzing and retrieving head-to-head team matchups.
 * Orchestrates the comparison of team rosters and handles the persistence of the results.
 */
@Service
public class TeamMatchupService {

    private final TeamMatchupRepository teamMatchupRepository;
    private final TeamMatchupAnalyzer teamMatchupAnalyzer;
    private final TeamMatchupMapper teamMatchupMapper;
    private final PlayerRepository playerRepository;

    public TeamMatchupService(TeamMatchupRepository teamMatchupRepository, TeamMatchupAnalyzer teamMatchupAnalyzer,
                              TeamMatchupMapper teamMatchupMapper, PlayerRepository playerRepository) {
        this.teamMatchupRepository = teamMatchupRepository;
        this.teamMatchupAnalyzer = teamMatchupAnalyzer;
        this.teamMatchupMapper = teamMatchupMapper;
        this.playerRepository = playerRepository;
    }

    /**
     * Retrieves a stored team matchup report by its ID.
     *
     * @param id The unique identifier of the team matchup record
     * @return A DTO containing the team matchup results
     * @throws MatchupNotFoundException if the matchup record is not found
     */
    public TeamMatchupResponseDTO getTeamMatchupById(long id) {
        TeamMatchupResultEntity entity = teamMatchupRepository.findById(id).orElseThrow(
                () -> new MatchupNotFoundException("Team matchup with id " + id + " does not exist")
        );

        return teamMatchupMapper.domainToResponse(teamMatchupMapper.entityToDomain(entity));
    }

    /**
     * Performs a head-to-head analysis between two teams and saves the result.
     * If a matchup between these exact two teams already exists, the stored result is returned.
     *
     * @param team1 The first fantasy team
     * @param team2 The second fantasy team
     * @return A DTO containing the analysis results, including player battles
     */
    public TeamMatchupResponseDTO createTeamMatchup(Team team1, Team team2) {

        Optional<TeamMatchupResultEntity> existing = teamMatchupRepository
                .findByTeam1AndTeam2(team1.getName(), team2.getName());

        if (existing.isPresent()) {
            return teamMatchupMapper.domainToResponse(teamMatchupMapper.entityToDomain(existing.get()));
        }

        TeamMatchupResult result = teamMatchupAnalyzer.analyzeTeamMatchup(team1, team2);
        TeamMatchupResultEntity entity = teamMatchupMapper.domainToEntity(result);

        for (int i = 0; i < result.getPlayerMatchupResults().size(); i++) {
            PlayerMatchupResult domainResult = result.getPlayerMatchupResults().get(i);
            PlayerMatchupResultEntity pmEntity = entity.getPlayerMatchupResults().get(i);

            pmEntity.setTeamMatchupResult(entity);
            attachPlayerToMatchup(pmEntity, domainResult);
        }

        TeamMatchupResultEntity savedEntity = teamMatchupRepository.save(entity);
        TeamMatchupResult savedDomain = teamMatchupMapper.entityToDomain(savedEntity);

        return teamMatchupMapper.domainToResponse(savedDomain);
    }

    private void attachPlayerToMatchup(PlayerMatchupResultEntity entityResult, PlayerMatchupResult domainResult) {
        playerRepository.findByNameAndNflTeam(domainResult.getPlayer1().getName(), domainResult.getPlayer1().getTeam())
                .ifPresent(p1 -> {
                    entityResult.setPlayer1(p1);
                    entityResult.setPlayer1ScareResult(p1.getScareResult());

                    if (domainResult.getWinner().isPresent() && domainResult.getWinner().get().getName().equals(p1.getName())) {
                        entityResult.setWinner(p1);
                    } else if (domainResult.getLoser().isPresent() && domainResult.getLoser().get().getName().equals(p1.getName())) {
                        entityResult.setLoser(p1);
                    }
                });

        playerRepository.findByNameAndNflTeam(domainResult.getPlayer2().getName(), domainResult.getPlayer2().getTeam())
                .ifPresent(p2 -> {
                    entityResult.setPlayer2(p2);
                    entityResult.setPlayer2ScareResult(p2.getScareResult());

                    if (domainResult.getWinner().isPresent() && domainResult.getWinner().get().getName().equals(p2.getName())) {
                        entityResult.setWinner(p2);
                    } else if (domainResult.getLoser().isPresent() && domainResult.getLoser().get().getName().equals(p2.getName())) {
                        entityResult.setLoser(p2);
                    }
                });
    }
}
