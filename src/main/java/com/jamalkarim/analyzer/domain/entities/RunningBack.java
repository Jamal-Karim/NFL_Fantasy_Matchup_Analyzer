package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

public class RunningBack extends Player{

    public static final double MAX_RUSHING_YARDS_PER_GAME = 125.0;
    public static final double MAX_RUSHING_TDS_PER_GAME = 1.1;
    public static final double MAX_RECEPTIONS_PER_GAME = 6.0;
    public static final double MAX_RECEIVING_YARDS_PER_GAME = 60.0;
    public static final double MAX_RECEIVING_TDS_PER_GAME = 0.5;

    public RunningBack(String name, String team) {
        super(name, team, Position.RB);
    }

    @Override
    public double calculateScareFactor() {
        BlendedStats stats = calculateStatBlendStrategy();

        double score = 0.0;
        score += (stats.getRushingYardsPerGame() / MAX_RUSHING_YARDS_PER_GAME) * 0.35;
        score += (stats.getRushingTDsPerGame() / MAX_RUSHING_TDS_PER_GAME) * 0.25;
        score += (stats.getReceptionsPerGame() / MAX_RECEPTIONS_PER_GAME) * 0.20;
        score += (stats.getReceivingYardsPerGame() / MAX_RECEIVING_YARDS_PER_GAME) * 0.15;
        score += (stats.getReceivingTDsPerGame() / MAX_RECEIVING_TDS_PER_GAME) * 0.05;

        return Math.max(0, score * 100);
    }
}
