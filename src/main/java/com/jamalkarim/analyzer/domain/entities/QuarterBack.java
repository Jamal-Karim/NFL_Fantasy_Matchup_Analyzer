package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

public class QuarterBack extends Player{

    public static final double MAX_PASSING_YARDS_PER_GAME = 253.0;
    public static final double MAX_PASSING_TDS_PER_GAME = 2.4;
    public static final double MAX_INTERCEPTIONS_PER_GAME = 1.0;
    public static final double MAX_RUSHING_YARDS_PER_GAME = 54.0;
    public static final double MAX_RUSHING_TDS_PER_GAME = 0.82;

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
        double completionPct = (stats.getCompletionsPerGame() / stats.getPassAttemptsPerGame());

        double score = 0.0;
        score += passingTDsPerGame / MAX_PASSING_TDS_PER_GAME * 0.25;
        score += passingYardsPerGame / MAX_PASSING_YARDS_PER_GAME * 0.20;
        score += completionPct * 0.05;
        score -= intsPerGame / MAX_INTERCEPTIONS_PER_GAME * 0.15;
        score += rushingTDsPerGame / MAX_RUSHING_TDS_PER_GAME * 0.20;
        score += rushingYardsPerGame / MAX_RUSHING_YARDS_PER_GAME * 0.15;

        return Math.max(0, score * 100);
    }
}
