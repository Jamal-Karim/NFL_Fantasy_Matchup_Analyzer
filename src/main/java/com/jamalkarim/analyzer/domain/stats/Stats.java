package com.jamalkarim.analyzer.domain.stats;

import lombok.Data;

/**
 * Represents the raw seasonal statistics for a player.
 * <p>
 * This object holds cumulative totals for passing, rushing, and receiving
 * statistics, along with metadata such as the season year and games played.
 */
@Data
public class Stats {

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
