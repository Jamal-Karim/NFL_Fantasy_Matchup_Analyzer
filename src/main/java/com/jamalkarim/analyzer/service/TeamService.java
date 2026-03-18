package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.requests.PlayerRequest;
import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import com.jamalkarim.analyzer.entities.TeamEntity;
import com.jamalkarim.analyzer.exceptions.PlayerAlreadyRosteredException;
import com.jamalkarim.analyzer.exceptions.TeamAlreadyExistsException;
import com.jamalkarim.analyzer.exceptions.TeamNotFoundException;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.repository.TeamRepository;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import com.jamalkarim.analyzer.utils.TeamMapper;
import org.springframework.stereotype.Service;

/**
 * Service for managing NFL fantasy teams.
 * Handles team creation, retrieval, and roster management.
 */
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

    /**
     * Retrieves a team by its unique database identifier.
     *
     * @param id The ID of the team record
     * @return The Team response object
     * @throws TeamNotFoundException if no team exists with the given ID
     */
    public TeamResponseDTO getTeamResponseById(long id) {
        return repository.findById(id)
                .map(mapper::entityToDomain)
                .map(mapper::domainToResponse)
                .orElseThrow(() -> new TeamNotFoundException(id));
    }

    /**
     * Retrieves a team by its unique database identifier.
     *
     * @param id The ID of the team record
     * @return The Team domain object
     * @throws TeamNotFoundException if no team exists with the given ID
     */
    public Team getTeamById(long id) {
        return repository.findById(id)
                .map(mapper::entityToDomain)
                .orElseThrow(() -> new TeamNotFoundException(id));
    }

    /**
     * Creates a new fantasy team based on the provided request.
     * If a team with the same name already exists, returns the existing team.
     * Validates that players added to the roster are not already assigned to another team.
     *
     * @param request The team creation request containing name and roster
     * @return The created or existing Team domain model
     * @throws TeamAlreadyExistsException     if a team already exists
     * @throws PlayerAlreadyRosteredException if a player is already assigned to another team
     */
    public TeamResponseDTO createTeam(TeamRequest request) {

        if (repository.findByName(request.getName()).isPresent()) {
            throw new TeamAlreadyExistsException("Team with name '" + request.getName() + "' already exists.");
        }

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
                    throw new PlayerAlreadyRosteredException(
                            livePlayer.getName() + " is already on team: " + livePlayer.getTeamEntity().getName()
                    );
                }
                teamEntity.addPlayer(livePlayer);
            });
        }

        TeamEntity savedEntity = repository.save(teamEntity);
        teamDomain.setId(savedEntity.getId());
        return mapper.domainToResponse(teamDomain);
    }
}
