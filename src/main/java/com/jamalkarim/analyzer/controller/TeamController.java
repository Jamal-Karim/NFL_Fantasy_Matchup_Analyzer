package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.service.TeamService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;


    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable long id) {
        return teamService.getTeamById(id);
    }

    @PostMapping("/create")
    public Team createTeam(@RequestBody TeamRequest request) {
        return teamService.createTeam(request);
    }
}
