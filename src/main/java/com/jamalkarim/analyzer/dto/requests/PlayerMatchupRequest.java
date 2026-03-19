package com.jamalkarim.analyzer.dto.requests;

import lombok.Data;

/**
 * Request DTO for initiating a player matchup analysis.
 * Contains the IDs of the two players to be compared.
 */
@Data
public class PlayerMatchupRequest {
    private long player1Id;
    private long player2Id;
}
