package com.jamalkarim.analyzer.dto.requests;

import lombok.Data;

import java.util.List;

/**
 * Request DTO for initiating a team creation.
 * Contains the name of the team and a list of player requests.
 */
@Data
public class TeamRequest {
    private String name;
    private List<PlayerRequest> roster;
}
