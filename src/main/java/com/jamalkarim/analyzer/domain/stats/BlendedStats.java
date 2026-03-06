package com.jamalkarim.analyzer.domain.stats;

import lombok.Data;

@Data
public class BlendedStats {

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
