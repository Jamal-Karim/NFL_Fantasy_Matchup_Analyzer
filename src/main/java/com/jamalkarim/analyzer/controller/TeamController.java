package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.RestResponse;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import com.jamalkarim.analyzer.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing NFL fantasy teams.
 * Provides endpoints for creating teams and retrieving team details.
 */
@RestController
@RequestMapping("/api/team")
@Tag(name = "2. Teams", description = "Roster management and fantasy team configuration")
public class TeamController {

    private final TeamService teamService;

    /**
     * Constructs a new TeamController with the required TeamService.
     *
     * @param teamService Service for managing fantasy team data
     */
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Creates a new fantasy team or returns the existing one if the name matches.
     *
     * @param request The TeamRequest containing team name and roster
     * @return An ApiResponse containing the created or existing TeamResponseDTO
     */
    @PostMapping("/create")
    @Operation(summary = "Create a new team",
            description = "Initializes a new fantasy team with a specific name and initial roster",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-order", value = "01")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully created team",
                    content = @Content(schema = @Schema(implementation = TeamResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid roster or team name", content = @Content)
    })
    public RestResponse<TeamResponseDTO> createTeam(@RequestBody TeamRequest request) {
        return RestResponse.success(teamService.createTeam(request));
    }

    /**
     * Retrieves a team by its unique identifier.
     *
     * @param id The ID of the team to retrieve
     * @return An ApiResponse containing the TeamResponseDTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get team by ID",
            description = "Retrieves details for a specific fantasy team",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-order", value = "02")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved team",
                    content = @Content(schema = @Schema(implementation = TeamResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Team ID not found", content = @Content)
    })
    public RestResponse<TeamResponseDTO> getTeamById(
            @Parameter(description = "Internal database ID of the team", required = true) @PathVariable long id) {
        return RestResponse.success(teamService.getTeamResponseById(id));
    }

    /**
     * Retrieves a paginated list of all fantasy teams.
     *
     * @param page The page number (0-indexed)
     * @param size The number of items per page
     * @return An ApiResponse containing a page of TeamResponseDTOs
     */
    @GetMapping
    @Operation(summary = "List all teams",
            description = "Retrieves a paginated list of all fantasy teams currently registered in the system.",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-order", value = "03")))
    public RestResponse<Page<TeamResponseDTO>> getAllTeams(
            @Parameter(description = "Zero-indexed page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size) {
        Page<TeamResponseDTO> teamPage = teamService.getAllTeams(page, size);
        return RestResponse.success(teamPage);
    }

    /**
     * Updates an existing fantasy team's name and roster.
     *
     * @param id      The unique identifier of the team to update
     * @param request The TeamRequest containing updated data
     * @return An ApiResponse containing the updated TeamResponseDTO
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update team details",
            description = "Modifies an existing team's name or roster. Performs full validation on the new roster state.",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-order", value = "08")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully created team",
                    content = @Content(schema = @Schema(implementation = TeamResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Team ID not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid update data", content = @Content)
    })
    public RestResponse<TeamResponseDTO> updateTeam(
            @Parameter(description = "Internal database ID of the team to update", required = true) @PathVariable long id,
            @RequestBody TeamRequest request) {
        return RestResponse.success(teamService.updateTeam(id, request));
    }

    /**
     * Deletes a fantasy team by its ID.
     *
     * @param id The unique identifier of the team to delete
     * @return An ApiResponse containing a success message
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete team",
            description = "Removes a fantasy team and its associated roster associations from the system.",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-order", value = "09")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted team"),
            @ApiResponse(responseCode = "404", description = "Team ID not found", content = @Content)
    })
    public RestResponse<String> deleteTeam(
            @Parameter(description = "Internal database ID of the team to delete", required = true) @PathVariable long id) {
        return RestResponse.success(teamService.deleteTeam(id));
    }
}
