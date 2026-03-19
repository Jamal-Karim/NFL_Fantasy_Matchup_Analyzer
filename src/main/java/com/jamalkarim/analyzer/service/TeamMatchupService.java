package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.matchups.TeamMatchupAnalyzer;
import com.jamalkarim.analyzer.domain.matchups.TeamMatchupResult;
import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.response.TeamMatchupResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import com.jamalkarim.analyzer.entities.TeamMatchupResultEntity;
import com.jamalkarim.analyzer.repository.PlayerMatchupRepository;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.repository.TeamMatchupRepository;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import com.jamalkarim.analyzer.utils.TeamMatchupMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamMatchupService {

    private final TeamMatchupRepository teamMatchupRepository;
    private final PlayerMatchupRepository playerMatchupRepository;
    private final TeamMatchupAnalyzer teamMatchupAnalyzer;
    private final TeamMatchupMapper teamMatchupMapper;
    private final PlayerRepository playerRepository;
    private final PlayerMatchupService playerMatchupService;
    private final PlayerMapper playerMapper;

    public TeamMatchupService(TeamMatchupRepository teamMatchupRepository, PlayerMatchupRepository playerMatchupRepository,
                              TeamMatchupAnalyzer teamMatchupAnalyzer, TeamMatchupMapper teamMatchupMapper, PlayerRepository playerRepository,
                              PlayerMatchupService playerMatchupService, PlayerMapper playerMapper) {
        this.teamMatchupRepository = teamMatchupRepository;
        this.playerMatchupRepository = playerMatchupRepository;
        this.teamMatchupAnalyzer = teamMatchupAnalyzer;
        this.teamMatchupMapper = teamMatchupMapper;
        this.playerRepository = playerRepository;
        this.playerMatchupService = playerMatchupService;
        this.playerMapper = playerMapper;
    }

    public TeamMatchupResponseDTO getTeamMatchupById(long id) {
        TeamMatchupResultEntity entity = teamMatchupRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Team matchup does not exist")
        );

        return teamMatchupMapper.domainToResponse(teamMatchupMapper.entityToDomain(entity));
    }

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
