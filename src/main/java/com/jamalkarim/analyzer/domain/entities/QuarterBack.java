package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

public class QuarterBack extends Player{

    public static final double MAX_PASSING_YARDS_PER_GAME = 235.0;
    public static final double MAX_PASSING_TDS_PER_GAME = 2.0;
    public static final double MAX_INTERCEPTIONS_PER_GAME = 1.0;
    public static final double MAX_RUSHING_YARDS_PER_GAME = 50.0;
    public static final double MAX_RUSHING_TDS_PER_GAME = 0.8;

    public QuarterBack(String name, String team) {
        super(name, team, Position.QB);
    }

    @Override
    public double calculateScareFactor() {
        BlendedStats stats = calculateStatBlendStrategy();

        double passingYardsPerGame = stats.getPassingYardsPerGame();
        double passingTDsPerGame = stats.getPassingTDsPerGame();
        double intsPerGame = stats.getIntsPerGame();
        double rushingYardsPerGame = stats.getRushingYardsPerGame();
        double rushingTDsPerGame = stats.getRushingTDsPerGame();

        double completionPct = (stats.getPassAttemptsPerGame() > 0)
                ? stats.getCompletionsPerGame() / stats.getPassAttemptsPerGame()
                : 0.0;

        double score = 0.0;

        score += (passingTDsPerGame / MAX_PASSING_TDS_PER_GAME) * 0.35;
        score += (passingYardsPerGame / MAX_PASSING_YARDS_PER_GAME) * 0.15;
        score += (completionPct * 0.05);

        score -= (intsPerGame / MAX_INTERCEPTIONS_PER_GAME) * 0.05;

        score += (rushingTDsPerGame / MAX_RUSHING_TDS_PER_GAME) * 0.35;
        score += (rushingYardsPerGame / MAX_RUSHING_YARDS_PER_GAME) * 0.15;

        return Math.max(0, applySoftCap(score * 125));
    }
}
