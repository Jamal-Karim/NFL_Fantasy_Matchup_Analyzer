package com.jamalkarim.analyzer.dto.requests;

import lombok.Data;

@Data
public class TeamMatchupRequest {
    private long team1Id;
    private long team2Id;
}
