package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.requests.PlayerMatchupRequest;
import com.jamalkarim.analyzer.dto.requests.TeamMatchupRequest;
import com.jamalkarim.analyzer.dto.response.ApiResponse;
import com.jamalkarim.analyzer.dto.response.PlayerMatchupResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamMatchupResponseDTO;
import com.jamalkarim.analyzer.service.PlayerMatchupService;
import com.jamalkarim.analyzer.service.PlayerService;
import com.jamalkarim.analyzer.service.TeamMatchupService;
import com.jamalkarim.analyzer.service.TeamService;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing head-to-head matchups.
 * Provides endpoints for analyzing and retrieving both player-to-player and team-to-team comparisons.
 */
@RestController
@RequestMapping("/api/matchup")
public class MatchupController {

    private final PlayerService playerService;
    private final TeamService teamService;
    private final PlayerMatchupService matchupService;
    private final TeamMatchupService teamMatchupService;

    /**
     * Constructs a new MatchupController with the required services.
     *
     * @param playerService       Service for managing player data
     * @param teamService         Service for managing team data
     * @param matchupService      Service for analyzing player matchups
     * @param teamMatchupService  Service for analyzing team matchups
     */
    public MatchupController(PlayerService playerService, TeamService teamService, PlayerMatchupService matchupService, TeamMatchupService teamMatchupService) {
        this.playerService = playerService;
        this.teamService = teamService;
        this.matchupService = matchupService;
        this.teamMatchupService = teamMatchupService;
    }


    /**
     * Initiates a head-to-head matchup analysis between two players.
     *
     * @param request A request containing the IDs of the two players to compare
     * @return An ApiResponse containing detailed matchup results
     */
    @PostMapping("/player/create")
    public ApiResponse<PlayerMatchupResponseDTO> createPlayerMatchup(@RequestBody PlayerMatchupRequest request) {
        Player player1 = playerService.getPlayerByID(request.getPlayer1Id());
        Player player2 = playerService.getPlayerByID(request.getPlayer2Id());

        PlayerMatchupResponseDTO response = matchupService.createPlayerMatchup(player1, player2);
        return ApiResponse.success(response);
    }

    /**
     * Retrieves an existing player matchup report by its ID.
     *
     * @param id The unique identifier for the matchup result
     * @return An ApiResponse containing the stored matchup details
     */
    @GetMapping("/player/{id:\\d+}")
    public ApiResponse<PlayerMatchupResponseDTO> getPlayerMatchupById(@PathVariable long id) {
        return ApiResponse.success(matchupService.getPlayerMatchupResponseById(id));
    }


    /**
     * Initiates a head-to-head matchup analysis between two teams.
     *
     * @param request A request containing the IDs of the two teams to compare
     * @return An ApiResponse containing detailed team matchup results
     */
    @PostMapping("/team/create")
    public ApiResponse<TeamMatchupResponseDTO> createTeamMatchupResult(@RequestBody TeamMatchupRequest request) {
        Team team1 = teamService.getTeamById(request.getTeam1Id());
        Team team2 = teamService.getTeamById(request.getTeam2Id());

        return ApiResponse.success(teamMatchupService.createTeamMatchup(team1, team2));
    }

    /**
     * Retrieves an existing team matchup report by its ID.
     *
     * @param id The unique identifier for the team matchup result
     * @return An ApiResponse containing the stored team matchup details
     */
    @GetMapping("/team/{id:\\d+}")
    public ApiResponse<TeamMatchupResponseDTO> getTeamMatchupById(@PathVariable long id) {
        return ApiResponse.success(teamMatchupService.getTeamMatchupById(id));
    }
}
