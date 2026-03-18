package com.jamalkarim.analyzer.controller;

import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.ApiResponse;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
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
    public ApiResponse<TeamResponseDTO> getTeamById(@PathVariable long id) {
        return ApiResponse.success(teamService.getTeamById(id));
    }

    @PostMapping("/create")
    public ApiResponse<TeamResponseDTO> createTeam(@RequestBody TeamRequest request) {
        return ApiResponse.success(teamService.createTeam(request));
    }
}
