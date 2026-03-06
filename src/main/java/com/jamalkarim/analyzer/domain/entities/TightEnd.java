package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

public class TightEnd extends Player{

    private static final double RECEPTION_BENCHMARK = 5.0;
    private static final double RECEIVING_YARD_BENCHMARK = 50.0;
    private static final double ELITE_RECEPTION_THRESHOLD = 7.0;

    public TightEnd(String name, String team) {
        super(name, team, Position.TE);
    }

    @Override
    public double calculateScareFactor() {

        BlendedStats blendedStats = this.calculateStatBlendStrategy();

        double receivingTDs = blendedStats.getReceivingTDsPerGame();
        double receptions = blendedStats.getReceptionsPerGame();
        double receivingYards = blendedStats.getReceivingYardsPerGame();

        double score = (receivingTDs * 0.4) + (receptions / RECEPTION_BENCHMARK * 0.35) +
                (receivingYards / RECEIVING_YARD_BENCHMARK * 0.25);

        if (receptions > ELITE_RECEPTION_THRESHOLD) {
            score *= 1.15;
        }

        return Math.max(0.0, score);
    }

}
