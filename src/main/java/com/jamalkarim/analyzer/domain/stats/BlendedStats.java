package com.jamalkarim.analyzer.domain.stats;

import lombok.Data;

/**
 * Represents a set of normalized, per-game statistics.
 * <p>
 * This object is the result of the {@link StatsBlender}'s logic, 
 * merging data from different seasons and league baselines into
 * a single predictive representation used for Scare Factor calculations.
 */
@Data
public class BlendedStats {

    private double passAttemptsPerGame;
    private double completionsPerGame;
    private double passingYardsPerGame;
    private double passingTDsPerGame;
    private double intsPerGame;

    private double rushingAttemptsPerGame;
    private double rushingYardsPerGame;
    private double rushingTDsPerGame;

    private double targetsPerGame;
    private double receptionsPerGame;
    private double receivingYardsPerGame;
    private double receivingTDsPerGame;
}
