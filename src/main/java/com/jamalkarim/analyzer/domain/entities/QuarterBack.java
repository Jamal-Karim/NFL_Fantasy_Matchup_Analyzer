package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

public class QuarterBack extends Player{

    private static final double PASSING_YARD_BENCHMARK = 300.0;
    private static final double RUSHING_YARD_BENCHMARK = 50.0;

    public QuarterBack(String name, String team) {
        super(name, team, Position.QB);
    }

    @Override
    public double calculateScareFactor() {
        BlendedStats blendedStats = this.calculateStatBlendStrategy();

        double passingYards = blendedStats.getPassingYardsPerGame();
        double passingTDs = blendedStats.getPassingTDsPerGame();
        double ints = blendedStats.getIntsPerGame();
        double rushingYards = blendedStats.getRushingYardsPerGame();
        double rushingTDs = blendedStats.getRushingTDsPerGame();

        return Math.max(0.0, (passingTDs * 0.3) + (passingYards / PASSING_YARD_BENCHMARK * 0.2)
                - (ints * 0.15) + (rushingTDs * 0.25) + (rushingYards / RUSHING_YARD_BENCHMARK * 0.1));
    }
}
