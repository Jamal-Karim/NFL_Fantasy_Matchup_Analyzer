package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.requests.PlayerMatchupRequest;
import com.jamalkarim.analyzer.dto.requests.TeamMatchupRequest;
import com.jamalkarim.analyzer.dto.response.RestResponse;
import com.jamalkarim.analyzer.dto.response.PlayerMatchupResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamMatchupResponseDTO;
import com.jamalkarim.analyzer.service.PlayerMatchupService;
import com.jamalkarim.analyzer.service.PlayerService;
import com.jamalkarim.analyzer.service.TeamMatchupService;
import com.jamalkarim.analyzer.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing head-to-head matchups.
 * Provides endpoints for analyzing and retrieving both player-to-player and team-to-team comparisons.
 */
@RestController
@RequestMapping("/api/matchup")
@Tag(name = "3. Matchups", description = "Head-to-head comparison and predictive performance metrics")
public class MatchupController {

    private final PlayerService playerService;
    private final TeamService teamService;
    private final PlayerMatchupService matchupService;
    private final TeamMatchupService teamMatchupService;

    /**
     * Constructs a new MatchupController with the required services.
     *
     * @param playerService      Service for managing player data
     * @param teamService        Service for managing team data
     * @param matchupService     Service for analyzing player matchups
     * @param teamMatchupService Service for analyzing team matchups
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
    @Operation(summary = "Analyze player vs player", description = "Performs a comparative analysis between two players, evaluating their Scare Factors and projectable stats for an upcoming matchup.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully generated player matchup"),
            @ApiResponse(responseCode = "404", description = "One or both players not found", content = @Content)
    })
    public RestResponse<PlayerMatchupResponseDTO> createPlayerMatchup(@RequestBody PlayerMatchupRequest request) {
        Player player1 = playerService.getPlayerByID(request.getPlayer1Id());
        Player player2 = playerService.getPlayerByID(request.getPlayer2Id());

        PlayerMatchupResponseDTO response = matchupService.createPlayerMatchup(player1, player2);
        return RestResponse.success(response);
    }

    /**
     * Retrieves an existing player matchup report by its ID.
     *
     * @param id The unique identifier for the matchup result
     * @return An ApiResponse containing the stored matchup details
     */
    @GetMapping("/player/{id:\\d+}")
    @Operation(summary = "Get player matchup by ID", description = "Retrieves a previously calculated player matchup report from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved matchup"),
            @ApiResponse(responseCode = "404", description = "Matchup ID not found", content = @Content)
    })
    public RestResponse<PlayerMatchupResponseDTO> getPlayerMatchupById(
            @Parameter(description = "Internal database ID of the player matchup", required = true) @PathVariable long id) {
        return RestResponse.success(matchupService.getPlayerMatchupResponseById(id));
    }


    /**
     * Initiates a head-to-head matchup analysis between two teams.
     *
     * @param request A request containing the IDs of the two teams to compare
     * @return An ApiResponse containing detailed team matchup results
     */
    @PostMapping("/team/create")
    @Operation(summary = "Analyze team vs team", description = "Performs a comprehensive roster comparison between two fantasy teams, identifying positional advantages and projected winner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully generated team matchup"),
            @ApiResponse(responseCode = "404", description = "One or both teams not found", content = @Content)
    })
    public RestResponse<TeamMatchupResponseDTO> createTeamMatchupResult(@RequestBody TeamMatchupRequest request) {
        Team team1 = teamService.getTeamById(request.getTeam1Id());
        Team team2 = teamService.getTeamById(request.getTeam2Id());

        return RestResponse.success(teamMatchupService.createTeamMatchup(team1, team2));
    }

    /**
     * Retrieves an existing team matchup report by its ID.
     *
     * @param id The unique identifier for the team matchup result
     * @return An ApiResponse containing the stored team matchup details
     */
    @GetMapping("/team/{id:\\d+}")
    @Operation(summary = "Get team matchup by ID", description = "Retrieves a previously calculated team matchup report from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved matchup"),
            @ApiResponse(responseCode = "404", description = "Matchup ID not found", content = @Content)
    })
    public RestResponse<TeamMatchupResponseDTO> getTeamMatchupById(
            @Parameter(description = "Internal database ID of the team matchup", required = true) @PathVariable long id) {
        return RestResponse.success(teamMatchupService.getTeamMatchupById(id));
    }
}
