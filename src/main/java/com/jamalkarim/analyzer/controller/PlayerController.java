package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.dto.response.ApiResponse;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.service.PlayerService;
import com.jamalkarim.analyzer.service.ScareResultService;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing and analyzing players and matchups.
 * Provides endpoints for retrieving player data, initiating head-to-head comparisons,
 * and performing detailed Scare Factor analysis.
 */
@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;
    private final ScareResultService scareResultService;

    public PlayerController(PlayerService playerService, ScareResultService scareResultService) {
        this.playerService = playerService;
        this.scareResultService = scareResultService;
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
    public ApiResponse<PlayerResponseDTO> getPlayerByName(@RequestParam String name, @PathVariable String nflTeam) {
        return ApiResponse.success(playerService.getOrSyncPlayer(name, nflTeam));
    }

    /**
     * Retrieves a player by their unique database identifier.
     *
     * @param id The ID of the player record
     * @return An ApiResponse containing player details
     */
    @GetMapping("/{id}")
    public ApiResponse<PlayerResponseDTO> getPlayerById(@PathVariable long id) {
        return ApiResponse.success(playerService.getPlayerResponseDTOByID(id));
    }

    /**
     * Retrieves a detailed Scare Factor analysis for a specific player.
     *
     * @param id The ID of the player to analyze
     * @return An ApiResponse containing the numerical score and descriptive reasoning
     */
    @GetMapping("/{id:\\d+}/analysis")
    public ApiResponse<ScareResponseDTO> getScareResultById(@PathVariable long id) {
        return ApiResponse.success(scareResultService.getScareResultById(id));
    }
}
