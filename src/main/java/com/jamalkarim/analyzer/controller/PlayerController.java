package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.dto.requests.MatchupRequest;
import com.jamalkarim.analyzer.dto.response.ApiResponse;
import com.jamalkarim.analyzer.dto.response.PlayerMatchupResponseDTO;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.service.PlayerMatchupService;
import com.jamalkarim.analyzer.service.PlayerService;
import com.jamalkarim.analyzer.service.ScareResultService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/team/{nflTeam}")
    public ApiResponse<PlayerResponseDTO> getPlayerByName(@RequestParam String name, @PathVariable String nflTeam) {
        return ApiResponse.success(playerService.getOrSyncPlayer(name, nflTeam));
    }

    @GetMapping("/{id}")
    public ApiResponse<PlayerResponseDTO> getPlayerById(@PathVariable long id) {
        return ApiResponse.success(playerService.getPlayerResponseDTOByID(id));
    }

    @PostMapping("/matchup/create")
    public ApiResponse<PlayerMatchupResponseDTO> createPlayerMatchup(@RequestBody MatchupRequest request) {
        Player player1 = playerService.getPlayerByID(request.getPlayer1Id());
        Player player2 = playerService.getPlayerByID(request.getPlayer2Id());

        PlayerMatchupResponseDTO response = matchupService.getPlayerMatchup(player1, player2);
        return ApiResponse.success(response);
    }

    @GetMapping("/matchup/{id:\\d+}")
    public ApiResponse<PlayerMatchupResponseDTO> getMatchupById(@PathVariable long id) {
        return ApiResponse.success(matchupService.getPlayerMatchupResponseById(id));
    }

    @GetMapping("/{id:\\d+}/analysis")
    public ApiResponse<ScareResponseDTO> getScareResultById(@PathVariable long id) {
        return ApiResponse.success(scareResultService.getScareResultById(id));
    }
}
