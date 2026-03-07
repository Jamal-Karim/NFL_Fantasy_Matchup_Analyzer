package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

public class RunningBack extends Player{

    public static final double MAX_RUSHING_YARDS_PER_GAME = 110.0;
    public static final double MAX_RUSHING_TDS_PER_GAME = 0.85;
    public static final double MAX_RECEPTIONS_PER_GAME = 5.0;
    public static final double MAX_RECEIVING_YARDS_PER_GAME = 40.0;
    public static final double MAX_RECEIVING_TDS_PER_GAME = 0.5;

    public RunningBack(String name, String team) {
        super(name, team, Position.RB);
    }

    @Override
    public double calculateScareFactor() {
        BlendedStats stats = calculateStatBlendStrategy();

        double rushingYards = stats.getRushingYardsPerGame();
        double rushingTDs = stats.getRushingTDsPerGame();
        double receptions = stats.getReceptionsPerGame();
        double receivingYards = stats.getReceivingYardsPerGame();
        double receivingTDs = stats.getReceivingTDsPerGame();

        double score = 0.0;

        score += (rushingYards / MAX_RUSHING_YARDS_PER_GAME) * 0.45;
        score += (rushingTDs / MAX_RUSHING_TDS_PER_GAME) * 0.30;

        score += (receptions / MAX_RECEPTIONS_PER_GAME) * 0.15;
        score += (receivingYards / MAX_RECEIVING_YARDS_PER_GAME) * 0.05;
        score += (receivingTDs / MAX_RECEIVING_TDS_PER_GAME) * 0.05;

        return Math.max(0, applySoftCap(score * 130));
    }
}
