package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

public class WideReceiver extends Player{

    private static final double RECEPTION_BENCHMARK = 5.0;
    private static final double RECEIVING_YARD_BENCHMARK = 50.0;
    private static final double TARGET_BENCHMARK = 5.0;

    public WideReceiver(String name, String team) {
        super(name, team, Position.WR);
    }

    @Override
    public double calculateScareFactor() {
        BlendedStats blendedStats = this.calculateStatBlendStrategy();

        double receivingTDs = blendedStats.getReceivingTDsPerGame();
        double receptions = blendedStats.getReceptionsPerGame();
        double receivingYards = blendedStats.getReceivingYardsPerGame();
        double targets = blendedStats.getTargetsPerGame();

        return Math.max(0.0, (receivingTDs * 0.35) + (receptions / RECEPTION_BENCHMARK * 0.3) +
                (receivingYards / RECEIVING_YARD_BENCHMARK * 0.25) + (targets / TARGET_BENCHMARK * 0.1));
    }

}
