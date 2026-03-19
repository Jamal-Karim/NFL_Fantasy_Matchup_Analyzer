package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.domain.matchups.TeamMatchupResult;
import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.requests.TeamMatchupRequest;
import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.ApiResponse;
import com.jamalkarim.analyzer.dto.response.TeamMatchupResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import com.jamalkarim.analyzer.service.TeamMatchupService;
import com.jamalkarim.analyzer.service.TeamService;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing NFL fantasy teams.
 * Provides endpoints for creating teams and retrieving team details.
 */
@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;
    private final TeamMatchupService teamMatchupService;

    public TeamController(TeamService teamService, TeamMatchupService teamMatchupService) {
        this.teamService = teamService;
        this.teamMatchupService = teamMatchupService;
    }

    /**
     * Retrieves a team by its unique identifier.
     *
     * @param id The ID of the team to retrieve
     * @return An ApiResponse containing the TeamResponseDTO
     */
    @GetMapping("/{id}")
    public ApiResponse<TeamResponseDTO> getTeamById(@PathVariable long id) {
        return ApiResponse.success(teamService.getTeamResponseById(id));
    }

    /**
     * Creates a new fantasy team or returns the existing one if the name matches.
     *
     * @param request The TeamRequest containing team name and roster
     * @return An ApiResponse containing the created or existing TeamResponseDTO
     */
    @PostMapping("/create")
    public ApiResponse<TeamResponseDTO> createTeam(@RequestBody TeamRequest request) {
        return ApiResponse.success(teamService.createTeam(request));
    }

    @PostMapping("/matchup/create")
    public ApiResponse<TeamMatchupResponseDTO> createTeamMatchupResult(@RequestBody TeamMatchupRequest request) {
        Team team1 = teamService.getTeamById(request.getTeam1Id());
        Team team2 = teamService.getTeamById(request.getTeam2Id());

        return ApiResponse.success(teamMatchupService.createTeamMatchup(team1, team2));
    }

    @GetMapping("/matchup/{id:\\d+}")
    public ApiResponse<TeamMatchupResponseDTO> getTeamMatchupById(@PathVariable long id) {
        return ApiResponse.success(teamMatchupService.getTeamMatchupById(id));
    }
}
