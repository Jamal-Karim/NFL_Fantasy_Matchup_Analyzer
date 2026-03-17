package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.requests.PlayerRequest;
import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.entities.TeamEntity;
import com.jamalkarim.analyzer.exceptions.TeamNotFoundException;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.repository.TeamRepository;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import com.jamalkarim.analyzer.utils.TeamMapper;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository repository;
    private final PlayerRepository playerRepository;
    private final TeamMapper mapper;
    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    public TeamService(TeamRepository repository, PlayerRepository playerRepository, TeamMapper mapper, PlayerService playerService, PlayerMapper playerMapper) {
        this.repository = repository;
        this.playerRepository = playerRepository;
        this.mapper = mapper;
        this.playerService = playerService;
        this.playerMapper = playerMapper;
    }

    public Team getTeamById(long id) {
        return repository.findById(id)
                .map(mapper::entityToDomain)
                .orElseThrow(() -> new TeamNotFoundException(id));
    }

    public Team createTeam(TeamRequest request) {
        if (repository.findByName(request.getName()).isPresent()) {
            return mapper.entityToDomain(repository.findByName(request.getName()).get());
        } else {

            Team teamDomain = new Team(request.getName());

            for (PlayerRequest pr : request.getRoster()) {
                PlayerResponseDTO dto = playerService.getOrSyncPlayer(pr.getName(), pr.getTeam());
                Player player = playerMapper.responseToDomain(dto);

                teamDomain.addPlayer(player);
            }

            TeamEntity teamEntity = new TeamEntity();
            teamEntity.setName(teamDomain.getName());

            for (Player p : teamDomain.getRoster()) {
                playerRepository.findById(p.getId()).ifPresent(livePlayer -> {

                    if (livePlayer.getTeamEntity() != null) {
                        throw new RuntimeException(
                                livePlayer.getName() + " is already on team: " + livePlayer.getTeamEntity().getName()
                        );
                    }
                    teamEntity.addPlayer(livePlayer);
                });
            }

            TeamEntity savedEntity = repository.save(teamEntity);
            teamDomain.setId(savedEntity.getId());
            return teamDomain;
        }
    }
}
