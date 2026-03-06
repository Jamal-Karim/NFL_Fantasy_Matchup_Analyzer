package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

public class TightEnd extends Player{

    public static final double MAX_RECEPTIONS_PER_GAME = 7.0;
    public static final double MAX_RECEIVING_YARDS_PER_GAME = 85.0;
    public static final double MAX_RECEIVING_TDS_PER_GAME = 0.7;

    public TightEnd(String name, String team) {
        super(name, team, Position.TE);
    }

    @Override
    public double calculateScareFactor() {
        BlendedStats stats = calculateStatBlendStrategy();

        double score = 0.0;
        score += (stats.getReceptionsPerGame() / MAX_RECEPTIONS_PER_GAME) * 0.40;
        score += (stats.getReceivingYardsPerGame() / MAX_RECEIVING_YARDS_PER_GAME) * 0.35;
        score += (stats.getReceivingTDsPerGame() / MAX_RECEIVING_TDS_PER_GAME) * 0.25;

        if (stats.getReceptionsPerGame() > MAX_RECEPTIONS_PER_GAME) {
            score += 0.05;
        }

        return Math.max(0, score * 100);
    }

}
