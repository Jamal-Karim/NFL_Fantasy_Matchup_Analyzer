package com.jamalkarim.analyzer.domain.models;

import com.jamalkarim.analyzer.domain.enums.PlayerStats;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

import java.util.*;

/**
 * Wide Receiver-specific implementation of the Scare Factor.
 * <p>
 * Scoring balances volume (receptions) with efficiency (yards)
 * and scoring threat (TDs).
 */
public class WideReceiver extends Player {

    /**
     * Max statistics for WRs determined from the most elite players
     * at that position from the 2023, 2024, and 2025 NFL season
     */
    public static final double MAX_RECEPTIONS_PER_GAME = 8.2;
    public static final double MAX_RECEIVING_YARDS_PER_GAME = 105.0;
    public static final double MAX_RECEIVING_TDS_PER_GAME = 0.80;

    public static final double RECEPTIONS_WEIGHT = 0.25;
    public static final double RECEIVING_YARDS_WEIGHT = 0.35;
    public static final double RECEIVING_TDS_WEIGHT = 0.40;

    public WideReceiver(String name, String team) {
        super(name, team, Position.WR);
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
    public double calculateScareFactor() {
        Map<PlayerStats, Impact> impactMap = generateImpactMap();

        double score = 0.0;
        score += impactMap.get(PlayerStats.Receptions).getPointsGained();
        score += impactMap.get(PlayerStats.ReceivingYards).getPointsGained();
        score += impactMap.get(PlayerStats.ReceivingTDs).getPointsGained();

        return Math.max(0, applySoftCap(score * 138));
    }

    @Override
    public List<String> generateListOfExplanations() {
        Map<PlayerStats, Double> topContributingFactors = findTopContributingScores(generateImpactMap());
        List<String> explanations = new LinkedList<>();

        for (Map.Entry<PlayerStats, Double> entry : topContributingFactors.entrySet()) {
            PlayerStats stat = entry.getKey();
            double value = entry.getValue();

            switch (stat) {
                case Receptions -> explanations.add(getReceptionsLabel(getTierForStatistic(value, RECEPTIONS_WEIGHT)));
                case ReceivingYards ->
                        explanations.add(getReceivingYardsLabel(getTierForStatistic(value, RECEIVING_YARDS_WEIGHT)));
                case ReceivingTDs ->
                        explanations.add(getReceivingTDsLabel(getTierForStatistic(value, RECEIVING_TDS_WEIGHT)));
            }
        }

        return explanations;
    }

    private String getReceptionsLabel(int tier) {
        return switch (tier) {
            case 1 -> "Elite target vacuum; a consistent chain-mover and reliable safety valve";
            case 2 -> "High-volume possession receiver with dependable hands";
            case 3 -> "Steady contributor; finds soft spots in the secondary regularly";
            case -1 -> "Struggles to create separation; rarely targeted in the progression";
            case -2 -> "Low-volume receiver; limited involvement in the weekly gameplan";
            case -3 -> "Inconsistent hands; high frequency of missed connections";
            default -> "Average reception volume";
        };
    }

    private String getReceivingYardsLabel(int tier) {
        return switch (tier) {
            case 1 -> "Elite vertical threat; capable of taking the top off any defense";
            case 2 -> "Highly productive playmaker with significant yardage upside";
            case 3 -> "Efficient yardage producer; gains chunk plays on intermediate routes";
            case -1 -> "Minimal downfield impact; yardage production is largely stalled";
            case -2 -> "Underwhelming air-yard share; struggles to generate big plays";
            case -3 -> "Limited explosive potential; strictly a short-yardage option";
            default -> "Average yardage production";
        };
    }

    private String getReceivingTDsLabel(int tier) {
        return switch (tier) {
            case 1 -> "Elite red zone weapon; a nightmare for cornerbacks in the end zone";
            case 2 -> "Dangerous scoring threat; consistently finds paydirt";
            case 3 -> "Reliable finisher on scoring drives";
            case -1 -> "Zero impact as a scoring threat; struggles to haul in touchdowns";
            case -2 -> "Rarely utilized in high-leverage scoring situations";
            case -3 -> "Low probability of finding the end zone; primarily a decoy in the red zone";
            default -> "Average touchdown frequency";
        };
    }
}
