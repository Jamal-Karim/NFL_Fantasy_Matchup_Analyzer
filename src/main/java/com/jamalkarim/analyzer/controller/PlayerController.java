package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.domain.matchups.PlayerMatchupResult;
import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.dto.requests.MatchupRequest;
import com.jamalkarim.analyzer.service.PlayerMatchupService;
import com.jamalkarim.analyzer.service.PlayerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMatchupService matchupService;

    public PlayerController(PlayerService playerService, PlayerMatchupService matchupService) {
        this.playerService = playerService;
        this.matchupService = matchupService;
    }

    @GetMapping("/team/{nflTeam}")
    public Player getPlayerByName(@RequestParam String name, @PathVariable String nflTeam) {
        return playerService.getOrSyncPlayer(name, nflTeam);
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
}
