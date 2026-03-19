package com.jamalkarim.analyzer.dto.requests;

import lombok.Data;

/**
 * Request DTO for initiating a team matchup analysis.
 * Contains the IDs of the two teams to be compared.
 */
@Data
public class TeamMatchupRequest {
    private long team1Id;
    private long team2Id;
}
