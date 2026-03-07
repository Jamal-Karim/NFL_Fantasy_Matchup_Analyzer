package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

public class TightEnd extends Player {

    public static final double MAX_RECEPTIONS_PER_GAME = 6.0;
    public static final double MAX_RECEIVING_YARDS_PER_GAME = 70.0;
    public static final double MAX_RECEIVING_TDS_PER_GAME = 0.6;

    public TightEnd(String name, String team) {
        super(name, team, Position.TE);
    }

    @Override
    public double calculateScareFactor() {
        BlendedStats stats = calculateStatBlendStrategy();

        double receptions = stats.getReceptionsPerGame();
        double yards = stats.getReceivingYardsPerGame();
        double tds = stats.getReceivingTDsPerGame();

        double score = 0.0;
        score += (receptions / MAX_RECEPTIONS_PER_GAME) * 0.40;
        score += (yards / MAX_RECEIVING_YARDS_PER_GAME) * 0.30;
        score += (tds / MAX_RECEIVING_TDS_PER_GAME) * 0.30;

        if (receptions > 6.5) {
            score += 0.10;
        }

        return Math.max(0, applySoftCap(score * 135));
    }
}
