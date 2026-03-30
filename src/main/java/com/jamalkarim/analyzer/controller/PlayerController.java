package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.dto.response.RestResponse;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.dto.response.SimulationResponseDTO;
import com.jamalkarim.analyzer.service.PlayerService;
import com.jamalkarim.analyzer.service.ScareResultService;
import com.jamalkarim.analyzer.service.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing and analyzing players and matchups.
 * Provides endpoints for retrieving player data and performing detailed Scare Factor analysis.
 */
@RestController
@RequestMapping("/api/player")
@Tag(name = "1. Players", description = "Management and analytical insights for NFL Players")
public class PlayerController {

    private final PlayerService playerService;
    private final ScareResultService scareResultService;
    private final SimulationService simulationService;

    /**
     * Constructs a new PlayerController with the required services.
     *
     * @param playerService      Service for managing player data
     * @param scareResultService Service for managing Scare Factor analysis
     */
    public PlayerController(PlayerService playerService, ScareResultService scareResultService, SimulationService simulationService) {
        this.playerService = playerService;
        this.scareResultService = scareResultService;
        this.simulationService = simulationService;
    }

    /**
     * Retrieves a player by their name and NFL team.
     * If the player is not found locally, the system will attempt to sync from an external provider.
     *
     * @param name    The full name of the player
     * @param nflTeam The abbreviation of the NFL team (e.g., "BUF", "SF")
     * @return An ApiResponse containing player details
     */
    @GetMapping("/team/{nflTeam}")
    @Operation(summary = "Search for a player",
            description = "Retrieves a player by name and NFL team. Automatically synchronizes data if not found.",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-order", value = "01")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found player", content = @Content(schema = @Schema(implementation = PlayerResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Could not find player", content = @Content())
    })
    public RestResponse<PlayerResponseDTO> getPlayerByName(
            @Parameter(description = "Full name of the player (e.g., 'Josh Allen')", required = true) @RequestParam String name,
            @Parameter(description = "Abbreviation of the NFL team (e.g., 'BUF')", required = true) @PathVariable String nflTeam) {
        return RestResponse.success(playerService.getOrSyncPlayer(name, nflTeam));
    }

    /**
     * Retrieves a player by their unique database identifier.
     *
     * @param id The ID of the player record
     * @return An ApiResponse containing player details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get player by ID",
            description = "Retrieves a specific player record using its internal database identifier.",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-order", value = "02")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved player"),
            @ApiResponse(responseCode = "404", description = "Player ID not found", content = @Content)
    })
    public RestResponse<PlayerResponseDTO> getPlayerById(
            @Parameter(description = "Internal database ID of the player", required = true) @PathVariable long id) {
        return RestResponse.success(playerService.getPlayerResponseDTOByID(id));
    }

    /**
     * Retrieves a detailed Scare Factor analysis for a specific player.
     *
     * @param id The ID of the player to analyze
     * @return An ApiResponse containing the numerical score and descriptive reasoning
     */
    @GetMapping("/{id:\\d+}/analysis")
    @Operation(summary = "Get Scare Factor Analysis",
            description = "Returns an analysis of a player with reasoning of how scary they are in a matchup",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-order", value = "03")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully generated analysis"),
            @ApiResponse(responseCode = "404", description = "Player ID not found", content = @Content)
    })
    public RestResponse<ScareResponseDTO> getScareResultById(
            @Parameter(description = "Internal database ID of the player to analyze", required = true) @PathVariable long id) {
        return RestResponse.success(scareResultService.getScareResultById(id));
    }

    /**
     * Executes a Monte Carlo simulation for a player to project their range of fantasy outcomes.
     * Runs 10,000 iterations based on the player's base stats and positional volatility.
     *
     * @param id The ID of the player to simulate
     * @return An ApiResponse containing the simulation results (floor, ceiling, boom/bust)
     */
    @GetMapping("/{id:\\d+}/simulation")
    @Operation(summary = "Run Monte Carlo Simulation for a Player",
            description = "Executes a Monte Carlo simulation across 10,000 iterations to project a player's range of outcomes",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-order", value = "04")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully generated simulation"),
            @ApiResponse(responseCode = "404", description = "Player ID not found", content = @Content)
    })
    public RestResponse<SimulationResponseDTO> getSimulationOfPlayerById(@PathVariable long id) {
        return RestResponse.success(simulationService.runSimulation(id));
    }

    /**
     * Retrieves a paginated list of all players, optionally filtered by position.
     * Results are sorted by Scare Factor in descending order.
     *
     * @param position Optional position filter (QB, RB, WR, TE)
     * @param page     The page number (0-indexed)
     * @param size     The number of items per page
     * @return An ApiResponse containing a page of player details
     */
    @GetMapping
    @Operation(summary = "List all players",
            description = "Retrieves a paginated list of all players currently in the system, ranked by scare factor descending.",
            extensions = @Extension(properties = @ExtensionProperty(name = "x-order", value = "05")))
    public RestResponse<Page<PlayerResponseDTO>> getAllPlayers(
            @Parameter(description = "Filter results by position (e.g., QB, RB)") @RequestParam(required = false) Position position,
            @Parameter(description = "Zero-indexed page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size) {
        Page<PlayerResponseDTO> playerPage = playerService.getAllPlayers(position, page, size);
        return RestResponse.success(playerPage);
    }
}
