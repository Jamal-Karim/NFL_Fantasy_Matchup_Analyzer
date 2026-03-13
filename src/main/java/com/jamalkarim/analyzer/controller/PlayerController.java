package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.dto.requests.MatchupRequest;
import com.jamalkarim.analyzer.dto.response.ApiResponse;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
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
    public Player getPlayerById(@PathVariable long id) {
        return playerService.getPlayerByID(id);
    }

    @PostMapping("/matchup/create")
    public PlayerMatchupResult createPlayerMatchup(@RequestBody MatchupRequest request) {
        Player player1 = playerService.getPlayerByID(request.getPlayer1Id());
        Player player2 = playerService.getPlayerByID(request.getPlayer2Id());

        return matchupService.getPlayerMatchup(player1, player2);
    }

    @GetMapping("/matchup/{id:\\d+}")
    public PlayerMatchupResult getMatchupById(@PathVariable long id) {
        return matchupService.getPlayerMatchupById(id);
    }

    @GetMapping("/{id:\\d+}/analysis")
    public ScareResult getScareResultById(@PathVariable long id) {
        return scareResultService.getScareResultById(id);
    }
}
