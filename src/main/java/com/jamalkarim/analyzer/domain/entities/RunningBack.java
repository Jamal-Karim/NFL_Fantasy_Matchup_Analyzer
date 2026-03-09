package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.PlayerStats;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

import java.util.HashMap;
import java.util.Map;

public class RunningBack extends Player{

    public static final double MAX_RUSHING_YARDS_PER_GAME = 110.0;
    public static final double MAX_RUSHING_TDS_PER_GAME = 0.85;
    public static final double MAX_RECEPTIONS_PER_GAME = 5.0;
    public static final double MAX_RECEIVING_YARDS_PER_GAME = 40.0;
    public static final double MAX_RECEIVING_TDS_PER_GAME = 0.5;

    public static final double RUSHING_YARDS_WEIGHT = 0.45;
    public static final double RUSHING_TDS_WEIGHT = 0.30;
    public static final double RECEPTIONS_WEIGHT = 0.15;
    public static final double RECEIVING_YARDS_WEIGHT = 0.05;
    public static final double RECEIVING_TDS_WEIGHT = 0.05;

    public RunningBack(String name, String team) {
        super(name, team, Position.RB);
    }

    @Override
    public double calculateScareFactor() {
        Map<PlayerStats, Impact> impactMap = generateImpactMap();

        double score = 0.0;

        score += impactMap.get(PlayerStats.RushingYards).getPointsGained();
        score += impactMap.get(PlayerStats.RushingTDs).getPointsGained();

        score += impactMap.get(PlayerStats.Receptions).getPointsGained();
        score += impactMap.get(PlayerStats.ReceivingYards).getPointsGained();
        score += impactMap.get(PlayerStats.ReceivingTDs).getPointsGained();

        return Math.max(0, applySoftCap(score * 130));
    }

    @Override
    protected Map<PlayerStats, Impact> generateImpactMap() {
        Map<PlayerStats, Impact> impactMap = new HashMap<>();
        BlendedStats stats = calculateStatBlendStrategy();

        double rushingYardsRatio = stats.getRushingYardsPerGame() / MAX_RUSHING_YARDS_PER_GAME;
        double rushingTDsRatio = stats.getRushingTDsPerGame() / MAX_RUSHING_TDS_PER_GAME;
        double receptionRatio = stats.getReceptionsPerGame() / MAX_RECEPTIONS_PER_GAME;
        double receivingYardsRatio = stats.getReceivingYardsPerGame() / MAX_RECEIVING_YARDS_PER_GAME;
        double receivingTDsRatio = stats.getReceivingTDsPerGame() / MAX_RECEIVING_TDS_PER_GAME;

        impactMap.put(PlayerStats.RushingYards, generateImpactForStat(rushingYardsRatio, RUSHING_YARDS_WEIGHT));

        impactMap.put(PlayerStats.RushingTDs, generateImpactForStat(rushingTDsRatio, RUSHING_TDS_WEIGHT));

        impactMap.put(PlayerStats.Receptions, generateImpactForStat(receptionRatio, RECEPTIONS_WEIGHT));

        impactMap.put(PlayerStats.ReceivingYards, generateImpactForStat(receivingYardsRatio, RECEIVING_YARDS_WEIGHT));

        impactMap.put(PlayerStats.ReceivingTDs, generateImpactForStat(receivingTDsRatio, RECEIVING_TDS_WEIGHT));

        return impactMap;
    }
}
