package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.ApiResponse;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import com.jamalkarim.analyzer.service.TeamService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing NFL fantasy teams.
 * Provides endpoints for creating teams and retrieving team details.
 */
@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
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

    /**
     * Retrieves a paginated list of all fantasy teams.
     *
     * @param page The page number (0-indexed)
     * @param size The number of items per page
     * @return An ApiResponse containing a page of TeamResponseDTOs
     */
    @GetMapping
    public ApiResponse<Page<TeamResponseDTO>> getAllTeams(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        Page<TeamResponseDTO> teamPage = teamService.getAllTeams(page, size);
        return ApiResponse.success(teamPage);
    }

    /**
     * Updates an existing fantasy team's name and roster.
     *
     * @param id      The unique identifier of the team to update
     * @param request The TeamRequest containing updated data
     * @return An ApiResponse containing the updated TeamResponseDTO
     */
    @PutMapping("/{id}")
    public ApiResponse<TeamResponseDTO> updateTeam(@PathVariable long id, @RequestBody TeamRequest request) {
        return ApiResponse.success(teamService.updateTeam(id, request));
    }

    /**
     * Deletes a fantasy team by its ID.
     *
     * @param id The unique identifier of the team to delete
     * @return An ApiResponse containing a success message
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteTeam(@PathVariable long id) {
        return ApiResponse.success(teamService.deleteTeam(id));
    }
}
