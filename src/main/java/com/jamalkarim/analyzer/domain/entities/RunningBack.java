package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

public class RunningBack extends Player{

    private static final double RUSHING_YARD_BENCHMARK = 50.0;
    private static final double RUSHING_ATTEMPT_BENCHMARK = 15.0;
    private static final double RECEPTION_BENCHMARK = 5.0;
    private static final double RECEIVING_YARD_BENCHMARK = 50.0;

    public RunningBack(String name, String team) {
        super(name, team, Position.RB);
    }

    @Override
    public double calculateScareFactor() {
        BlendedStats blendedStats = this.calculateStatBlendStrategy();

        double rushingYards = blendedStats.getRushingYardsPerGame();
        double rushingTDs = blendedStats.getRushingTDsPerGame();
        double rushingAttempts = blendedStats.getRushingAttemptsPerGame();
        double receptions = blendedStats.getReceptionsPerGame();
        double receivingYards = blendedStats.getReceivingYardsPerGame();

        return Math.max(0.0, (rushingTDs * 0.3) + (rushingYards / RUSHING_YARD_BENCHMARK * 0.25) +
                (rushingAttempts / RUSHING_ATTEMPT_BENCHMARK * 0.1) + (receptions / RECEPTION_BENCHMARK * 0.2)
                + (receivingYards / RECEIVING_YARD_BENCHMARK * 0.15));
    }
}
