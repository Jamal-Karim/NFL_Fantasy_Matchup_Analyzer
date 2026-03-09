package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.PlayerStats;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

import java.util.*;

public class QuarterBack extends Player{

    public static final double MAX_PASSING_YARDS_PER_GAME = 235.0;
    public static final double MAX_PASSING_TDS_PER_GAME = 2.0;
    public static final double MAX_INTERCEPTIONS_PER_GAME = 1.0;
    public static final double MAX_RUSHING_YARDS_PER_GAME = 50.0;
    public static final double MAX_RUSHING_TDS_PER_GAME = 0.8;

    public static final double PASSING_YARDS_WEIGHT = 0.15;
    public static final double PASSING_TDS_WEIGHT = 0.35;
    public static final double INT_WEIGHT = 0.05;
    public static final double COMPLETION_WEIGHT = 0.05;
    public static final double RUSHING_YARDS_WEIGHT = 0.15;
    public static final double RUSHING_TDS_WEIGHT = 0.35;

    public QuarterBack(String name, String team) {
        super(name, team, Position.QB);
    }

    @Override
    public double calculateScareFactor() {
        Map<PlayerStats, Impact> impactMap = generateImpactMap();

        double score = 0.0;

        score += impactMap.get(PlayerStats.PassingTDs).getPointsGained();
        score += impactMap.get(PlayerStats.PassingYards).getPointsGained();
        score += impactMap.get(PlayerStats.Completion).getPointsGained();

        score -= impactMap.get(PlayerStats.Interceptions).getPointsGained();

        score += impactMap.get(PlayerStats.RushingTDs).getPointsGained();
        score += impactMap.get(PlayerStats.RushingYards).getPointsGained();

        return Math.max(0, applySoftCap(score * 125));
    }

    protected Map<PlayerStats, Impact> generateImpactMap(){
        Map<PlayerStats, Impact> impactMap = new HashMap<>();
        BlendedStats stats = calculateStatBlendStrategy();

        double passingYardsRatio = stats.getPassingYardsPerGame() / MAX_PASSING_YARDS_PER_GAME;
        double passingTDsRatio = stats.getPassingTDsPerGame() / MAX_PASSING_TDS_PER_GAME;
        double intsRatio = stats.getIntsPerGame() / MAX_INTERCEPTIONS_PER_GAME;
        double rushingYardsRatio = stats.getRushingYardsPerGame() / MAX_RUSHING_YARDS_PER_GAME;
        double rushingTDsRatio = stats.getRushingTDsPerGame() / MAX_RUSHING_TDS_PER_GAME;
        double completionPct = (stats.getPassAttemptsPerGame() > 0)
                ? stats.getCompletionsPerGame() / stats.getPassAttemptsPerGame()
                : 0.0;

        impactMap.put(PlayerStats.PassingYards, generateImpactForStat(passingYardsRatio, PASSING_YARDS_WEIGHT));

        impactMap.put(PlayerStats.PassingTDs, generateImpactForStat(passingTDsRatio, PASSING_TDS_WEIGHT));

        impactMap.put(PlayerStats.Interceptions, generateImpactForStat(intsRatio, INT_WEIGHT));

        impactMap.put(PlayerStats.RushingYards, generateImpactForStat(rushingYardsRatio, RUSHING_YARDS_WEIGHT));

        impactMap.put(PlayerStats.RushingTDs, generateImpactForStat(rushingTDsRatio, RUSHING_TDS_WEIGHT));

        impactMap.put(PlayerStats.Completion, generateImpactForStat(completionPct, COMPLETION_WEIGHT));

        return impactMap;
    }
}
