package com.jamalkarim.analyzer.dto.requests;

import lombok.Data;

@Data
public class MatchupRequest {
    private long player1Id;
    private long player2Id;
}
