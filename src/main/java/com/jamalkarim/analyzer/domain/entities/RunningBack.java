package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.PlayerStats;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;

import java.util.*;

/**
 * Running Back-specific implementation of the Scare Factor.
 * 
 * Scoring is heavily weighted towards rushing volume and touchdown 
 * production, with a secondary focus on receiving involvement.
 */
public class RunningBack extends Player{

    /** Max statistics for RBs determined from the most elite players
     * at that position from the 2023, 2024, and 2025 NFL season */
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
    public List<String> generateListOfExplanations() {
        Map<PlayerStats, Double> topContributingFactors = findTopContributingScores(generateImpactMap());
        List<String> explanations = new LinkedList<>();

        for(Map.Entry<PlayerStats, Double> entry : topContributingFactors.entrySet()){
            PlayerStats stat = entry.getKey();
            Double value = entry.getValue();
            switch (stat) {
                case RushingYards   -> explanations.add(getRushingYardsLabel(getTierForStatistic(value, RUSHING_YARDS_WEIGHT)));
                case RushingTDs     -> explanations.add(getRushingTDsLabel(getTierForStatistic(value, RUSHING_TDS_WEIGHT)));
                case Receptions     -> explanations.add(getReceptionsLabel(getTierForStatistic(value, RECEPTIONS_WEIGHT)));
                case ReceivingYards -> explanations.add(getReceivingYardsLabel(getTierForStatistic(value, RECEIVING_YARDS_WEIGHT)));
                case ReceivingTDs   -> explanations.add(getReceivingTDsLabel(getTierForStatistic(value, RECEIVING_TDS_WEIGHT)));
            }
        }
        return explanations;

    }

    private String getRushingYardsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Elite workhorse back; consistently tears through the second level";
            case 2  -> "Highly productive lead runner with strong yardage output";
            case 3  -> "Reliable chain-mover; steady presence in the run game";
            case -1 -> "Struggles to find daylight; frequently bottled up at the line";
            case -2 -> "Inconsistent rushing production; yardage is hard to come by";
            case -3 -> "Low-volume runner with limited explosive potential";
            default -> "Average rushing impact";
        };
    }

    private String getRushingTDsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Elite goal-line hammer; a nightmare to stop in short-yardage";
            case 2  -> "Lethal red zone threat; consistently finds the end zone";
            case 3  -> "Reliable finisher when the offense gets close to the stripe";
            case -1 -> "Zero goal-line impact; struggles to punch it in";
            case -2 -> "Rarely utilized as a scoring option in the red zone";
            case -3 -> "Difficulty converting rushing attempts into scores";
            default -> "Average rushing touchdown impact";
        };
    }

    private String getReceptionsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Elite receiving back; a vital mismatch in the passing game";
            case 2  -> "High-volume safety valve; reliable hands out of the backfield";
            case 3  -> "Consistent check-down option for the quarterback";
            case -1 -> "Non-factor in the passing game; strictly a standard runner";
            case -2 -> "Limited involvement as a receiver; rarely targeted";
            case -3 -> "Minimal impact on passing downs";
            default -> "Average reception volume";
        };
    }

    private String getReceivingYardsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Dangerous playmaker in space; elite yardage after the catch";
            case 2  -> "Productive receiving threat; creates significant chunk plays";
            case 3  -> "Efficient at picking up yards through the air";
            case -1 -> "Negligible production as a receiver; low yardage upside";
            case -2 -> "Struggles to gain traction in the passing attack";
            case -3 -> "Limited contribution to the team's air yards";
            default -> "Average receiving yardage";
        };
    }

    private String getReceivingTDsLabel(int tier) {
        return switch (tier) {
            case 1  -> "Elite multi-dimensional scorer; dangerous target in the red zone";
            case 2  -> "Aggressive scoring threat through the air";
            case 3  -> "Occasional threat to haul in a touchdown pass";
            case -1 -> "Zero impact as a receiving scorer";
            case -2 -> "Rarely targeted for touchdowns in the passing game";
            case -3 -> "Low probability of scoring via the air";
            default -> "Average receiving touchdown impact";
        };
    }
}
