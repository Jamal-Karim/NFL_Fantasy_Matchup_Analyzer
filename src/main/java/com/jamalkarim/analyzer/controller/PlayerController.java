package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.dto.requests.PlayerMatchupRequest;
import com.jamalkarim.analyzer.dto.response.ApiResponse;
import com.jamalkarim.analyzer.dto.response.PlayerMatchupResponseDTO;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.service.PlayerMatchupService;
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
    private final PlayerMatchupService matchupService;
    private final ScareResultService scareResultService;

    public PlayerController(PlayerService playerService, PlayerMatchupService matchupService, ScareResultService scareResultService) {
        this.playerService = playerService;
        this.matchupService = matchupService;
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
     * Initiates a head-to-head matchup analysis between two players.
     *
     * @param request A request containing the IDs of the two players to compare
     * @return An ApiResponse containing detailed matchup results
     */
    @PostMapping("/matchup/create")
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
    @GetMapping("/matchup/{id:\\d+}")
    public ApiResponse<PlayerMatchupResponseDTO> getMatchupById(@PathVariable long id) {
        return ApiResponse.success(matchupService.getPlayerMatchupResponseById(id));
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
