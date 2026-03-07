package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

public class WideReceiver extends Player{

    public static final double MAX_RECEPTIONS_PER_GAME = 8.2;
    public static final double MAX_RECEIVING_YARDS_PER_GAME = 105.0;
    public static final double MAX_RECEIVING_TDS_PER_GAME = 0.80;
    public static final double MAX_RUSHING_YARDS_PER_GAME = 15.0;

    public WideReceiver(String name, String team) {
        super(name, team, Position.WR);
    }

    @Override
    public double calculateScareFactor() {
        BlendedStats stats = calculateStatBlendStrategy();

        double receptions = stats.getReceptionsPerGame();
        double yards = stats.getReceivingYardsPerGame();
        double tds = stats.getReceivingTDsPerGame();
        double rushYards = stats.getRushingYardsPerGame();

        double score = 0.0;
        score += (receptions / MAX_RECEPTIONS_PER_GAME) * 0.25;
        score += (yards / MAX_RECEIVING_YARDS_PER_GAME) * 0.35;
        score += (tds / MAX_RECEIVING_TDS_PER_GAME) * 0.35;
        score += (rushYards / MAX_RUSHING_YARDS_PER_GAME) * 0.05;

        return Math.max(0, applySoftCap(score * 138));
    }

}
