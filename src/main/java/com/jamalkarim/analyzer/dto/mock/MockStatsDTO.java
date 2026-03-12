package com.jamalkarim.analyzer.dto.mock;

import lombok.Data;

@Data
public class MockStatsDTO {
    private int season;
    private int gamesPlayed;

    private int passAttempts;
    private int completions;
    private int passingYards;
    private int passingTDs;
    private int interceptions;

    private int rushingAttempts;
    private int rushingYards;
    private int rushingTDs;

    private int targets;
    private int receptions;
    private int receivingYards;
    private int receivingTDs;
}
