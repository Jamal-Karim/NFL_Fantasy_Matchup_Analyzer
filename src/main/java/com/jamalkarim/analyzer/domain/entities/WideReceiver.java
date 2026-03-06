package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

public class WideReceiver extends Player{

    public static final double MAX_RECEPTIONS_PER_GAME = 8.0;
    public static final double MAX_RECEIVING_YARDS_PER_GAME = 105.0;
    public static final double MAX_RECEIVING_TDS_PER_GAME = 1.0;
    public static final double MAX_RUSHING_YARDS_PER_GAME = 15.0;

    public WideReceiver(String name, String team) {
        super(name, team, Position.WR);
    }

    @Override
    public double calculateScareFactor() {
        BlendedStats stats = calculateStatBlendStrategy();

        double score = 0.0;
        score += (stats.getReceptionsPerGame() / MAX_RECEPTIONS_PER_GAME) * 0.40;
        score += (stats.getReceivingYardsPerGame() / MAX_RECEIVING_YARDS_PER_GAME) * 0.30;
        score += (stats.getReceivingTDsPerGame() / MAX_RECEIVING_TDS_PER_GAME) * 0.20;
        score += (stats.getRushingYardsPerGame() / MAX_RUSHING_YARDS_PER_GAME) * 0.10;

        return Math.max(0, score * 100);
    }

}
