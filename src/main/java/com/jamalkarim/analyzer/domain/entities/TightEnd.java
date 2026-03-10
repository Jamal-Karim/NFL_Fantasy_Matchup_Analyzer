package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.PlayerStats;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

import java.util.*;

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

    @Override
    public List<String> generateListOfExplanations() {
        Map<PlayerStats, Double> topContributingFactors = findTopContributingScores(generateImpactMap());
        List<String> explanations = new LinkedList<>();

        for (Map.Entry<PlayerStats, Double> entry : topContributingFactors.entrySet()) {
            PlayerStats stat = entry.getKey();
            double value = entry.getValue();

            switch (stat) {
                case Receptions     -> explanations.add(getReceptionsLabel(getTierForStatistic(value, RECEPTIONS_WEIGHT)));
                case ReceivingYards -> explanations.add(getReceivingYardsLabel(getTierForStatistic(value, RECEIVING_YARDS_WEIGHT)));
                case ReceivingTDs   -> explanations.add(getReceivingTDsLabel(getTierForStatistic(value, RECEIVING_TDS_WEIGHT)));
            }
        }

        return explanations;
    }

    private String getReceptionsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Elite volume producer; a high-frequency mismatch in the middle of the field";
            case 2  -> "Highly reliable safety valve; consistently moves the chains on third down";
            case 3  -> "Steady target; finds open windows in zone coverage regularly";
            case -1 -> "Struggles to get open against physical coverage; rarely targeted";
            case -2 -> "Low involvement in the passing game; primarily used for blocking";
            case -3 -> "Inconsistent connection with the QB; limited volume in the progression";
            default -> "Average reception volume";
        };
    }

    private String getReceivingYardsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Elite seam threat; consistently gouges defenses for massive chunk plays";
            case 2  -> "Productive yardage generator; creates significant problems for linebackers";
            case 3  -> "Efficient yardage producer on intermediate routes";
            case -1 -> "Negligible downfield impact; yardage production is largely nonexistent";
            case -2 -> "Limited air-yard share; struggles to generate yards after the catch";
            case -3 -> "Minimal explosive potential; strictly a short-area check-down option";
            default -> "Average yardage production";
        };
    }

    private String getReceivingTDsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Elite red zone mismatch; too big for corners and too fast for linebackers";
            case 2  -> "Dangerous scoring threat; consistently hauls in touchdowns in traffic";
            case 3  -> "Reliable finisher in the painted area";
            case -1 -> "Zero impact as a scoring threat; non-factor in the end zone";
            case -2 -> "Rarely utilized as a target in high-leverage scoring situations";
            case -3 -> "Low probability of scoring; primarily serves as an extra blocker in the red zone";
            default -> "Average touchdown frequency";
        };
    }
}
