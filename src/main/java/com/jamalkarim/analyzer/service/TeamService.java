package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.requests.PlayerRequest;
import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.TeamEntity;
import com.jamalkarim.analyzer.exceptions.PlayerAlreadyRosteredException;
import com.jamalkarim.analyzer.exceptions.PlayerSyncException;
import com.jamalkarim.analyzer.exceptions.TeamAlreadyExistsException;
import com.jamalkarim.analyzer.exceptions.TeamNotFoundException;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.repository.TeamRepository;
import com.jamalkarim.analyzer.utils.PlayerMapper;
import com.jamalkarim.analyzer.utils.TeamMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * Constructs a TeamService with required dependencies.
     *
     * @param repository       The repository for fantasy team data
     * @param playerRepository The repository for player data
     * @param mapper           The mapper for fantasy team models
     * @param playerService    The service for managing individual players
     * @param playerMapper     The mapper for player models
     */
    public TeamService(TeamRepository repository, PlayerRepository playerRepository, TeamMapper mapper, PlayerService playerService, PlayerMapper playerMapper) {
        this.repository = repository;
        this.playerRepository = playerRepository;
        this.mapper = mapper;
        this.playerService = playerService;
        this.playerMapper = playerMapper;
    }

    /**
     * Retrieves a paginated list of all fantasy teams.
     *
     * @param page The page number to retrieve
     * @param size The number of records per page
     * @return A page of team response DTOs
     */
    public Page<TeamResponseDTO> getAllTeams(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size
        );

        Page<TeamEntity> entities;
        entities = repository.findAll(pageable);
        return entities.map(mapper::entityToDomain).map(mapper::domainToResponse);
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
    @Transactional
    public TeamResponseDTO createTeam(TeamRequest request) {

        if (repository.findByName(request.getName()).isPresent()) {
            throw new TeamAlreadyExistsException("Team with name '" + request.getName() + "' already exists.");
        }
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setName(request.getName());
        teamEntity = repository.save(teamEntity);

        addRosterToEntity(teamEntity, request.getRoster());

        return mapper.entityToResponse(repository.save(teamEntity));
    }

    /**
     * Updates an existing team's name and roster.
     * Existing roster is cleared before adding the new set of players.
     *
     * @param id      The ID of the team to update
     * @param request The TeamRequest containing the new name and players
     * @return The updated Team response object
     * @throws TeamNotFoundException if the team does not exist
     */
    @Transactional
    public TeamResponseDTO updateTeam(long id, TeamRequest request) {
        TeamEntity teamEntity = repository.findById(id).orElseThrow(
                () -> new TeamNotFoundException(id)
        );

        teamEntity.clearRoster();

        teamEntity.setName(request.getName());

        addRosterToEntity(teamEntity, request.getRoster());

        return mapper.entityToResponse(repository.save(teamEntity));
    }

    /**
     * Deletes a team and removes all player associations from it.
     *
     * @param id The ID of the team to delete
     * @return A success message
     * @throws TeamNotFoundException if the team does not exist
     */
    @Transactional
    public String deleteTeam(long id) {
        TeamEntity teamEntity = repository.findById(id).orElseThrow(
                () -> new TeamNotFoundException(id)
        );

        teamEntity.clearRoster();
        String name = teamEntity.getName();
        repository.delete(teamEntity);
        return "Successfully deleted team " + name;
    }

    /**
     * Helper method to sync players and add them to a team entity.
     * Enforces roster construction rules using the Team domain model.
     *
     * @param teamEntity The team receiving the players
     * @param roster     The list of player requests to process
     */
    private void addRosterToEntity(TeamEntity teamEntity, List<PlayerRequest> roster) {
        Team domainTeam = mapper.entityToDomain(teamEntity);

        for (PlayerRequest pr : roster) {
            PlayerResponseDTO dto = playerService.getOrSyncPlayer(pr.getName(), pr.getTeam());

            PlayerEntity playerEntity = playerRepository.findById(dto.getId())
                    .orElseThrow(() -> new PlayerSyncException("Player sync failed for ID " + dto.getId()));

            if (playerEntity.getTeamEntity() != null && !playerEntity.getTeamEntity().getId().equals(teamEntity.getId())) {
                throw new PlayerAlreadyRosteredException(
                        playerEntity.getName() + " is already on team: " + playerEntity.getTeamEntity().getName()
                );
            }

            domainTeam.addPlayer(playerMapper.entityToDomain(playerEntity));
            teamEntity.addPlayer(playerEntity);
        }
    }
}
