package com.jamalkarim.analyzer.domain.stats;

import lombok.Data;

@Data
public class Stats {

    private int season;
    private int gamesPlayed;

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
