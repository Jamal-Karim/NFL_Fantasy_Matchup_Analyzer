package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.PlayerStats;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

import java.util.HashMap;
import java.util.Map;

public class TightEnd extends Player {

    public static final double MAX_RECEPTIONS_PER_GAME = 6.0;
    public static final double MAX_RECEIVING_YARDS_PER_GAME = 70.0;
    public static final double MAX_RECEIVING_TDS_PER_GAME = 0.6;

    public static final double RECEPTIONS_WEIGHT = 0.40;
    public static final double RECEIVING_YARDS_WEIGHT = 0.30;
    public static final double RECEIVING_TDS_WEIGHT = 0.30;

    public TightEnd(String name, String team) {
        super(name, team, Position.TE);
    }

    @Override
    public double calculateScareFactor() {
        BlendedStats stats = calculateStatBlendStrategy();
        Map<PlayerStats, Impact> impactMap = generateImpactMap();

        double score = 0.0;
        score += impactMap.get(PlayerStats.Receptions).getPointsGained();
        score += impactMap.get(PlayerStats.ReceivingYards).getPointsGained();
        score += impactMap.get(PlayerStats.ReceivingTDs).getPointsGained();

        double receptions = stats.getReceptionsPerGame();
        if (receptions > 6.5) {
            score += 0.10;
        }

        return Math.max(0, applySoftCap(score * 135));
    }

    @Override
    protected Map<PlayerStats, Impact> generateImpactMap() {
        Map<PlayerStats, Impact> impactMap = new HashMap<>();
        BlendedStats stats = calculateStatBlendStrategy();

        double receptionRatio = stats.getReceptionsPerGame() / MAX_RECEPTIONS_PER_GAME;
        double receivingYardsRatio = stats.getReceivingYardsPerGame() / MAX_RECEIVING_YARDS_PER_GAME;
        double receivingTDsRatio = stats.getReceivingTDsPerGame() / MAX_RECEIVING_TDS_PER_GAME;

        impactMap.put(PlayerStats.Receptions, generateImpactForStat(receptionRatio, RECEPTIONS_WEIGHT));

        impactMap.put(PlayerStats.ReceivingYards, generateImpactForStat(receivingYardsRatio, RECEIVING_YARDS_WEIGHT));

        impactMap.put(PlayerStats.ReceivingTDs, generateImpactForStat(receivingTDsRatio, RECEIVING_TDS_WEIGHT));

        return impactMap;
    }
}
