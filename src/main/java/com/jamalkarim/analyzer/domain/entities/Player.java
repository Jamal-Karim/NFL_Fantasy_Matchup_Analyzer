package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.PlayerStats;
import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.BlendedStats;
import com.jamalkarim.analyzer.domain.scoring.ScareFactor;
import com.jamalkarim.analyzer.domain.stats.Stats;
import com.jamalkarim.analyzer.domain.stats.StatsBlender;
import lombok.Data;
import lombok.Getter;

import java.util.*;

@Data
public abstract class Player implements ScareFactor {

    private static final StatsBlender STATS_BLENDER = new StatsBlender();
    private static final int MIN_GAMES_FOR_TREND = 7;

    private String name;
    private String team;
    private Position position;
    private int draftPick;
    private boolean isRookie;
    private boolean isInjured;
    private PlayerTier tier;
    private Stats currentSeasonStats;
    private Stats lastSeasonStats;

    public Player(String name, String team, Position position) {
        this.name = name;
        this.team = team;
        this.position = position;
    }

    /**
     * Determines the most appropriate statistical blending strategy based on the player's
     * career stage and data availability.
     * <p>
     * The strategy priority is:
     * 1. Rookie: Weighted by draft position.
     * 2. Standard: Blends last season and current season (weighted by games played).
     * 3. Single Season: Used when only one season of reliable data exists.
     * 4. Injured/Baseline: Default fallback using league-average baselines.
     *
     * @return A BlendedStats object containing normalized per-game statistics.
     */
    public BlendedStats calculateStatBlendStrategy() {

        // Handle rookie case first as top priority
        if (isRookie) {
            return STATS_BLENDER.rookieBlend(position, draftPick);
        }

        int currentGames = (currentSeasonStats != null) ? currentSeasonStats.getGamesPlayed() : 0;
        int lastGames = (lastSeasonStats != null) ? lastSeasonStats.getGamesPlayed() : 0;

        boolean hasLastSeasonData = lastGames >= MIN_GAMES_FOR_TREND;
        boolean hasCurrentSeasonData = currentGames > 0;

        // If it is a normal player playing in the current season, calculate blend off of last season and current season
        if (hasCurrentSeasonData && hasLastSeasonData) {
            return STATS_BLENDER.standardBlend(lastSeasonStats, currentSeasonStats);
        }

        // If it is a player who has not played any games in the current season but has played in the previous season
        // calculate per game data off of just last season
        // use case for if calculating matchup before current season
        if (hasLastSeasonData) {
            return STATS_BLENDER.singleSeasonBlend(lastSeasonStats);
        }

        // If it is a player who does not have any data in the previous season but has played games in the current season
        // use case for a rookie or an injured player who has gotten to play games in the current season
        if (hasCurrentSeasonData) {
            return STATS_BLENDER.singleSeasonBlend(currentSeasonStats);
        }

        // Default to using the injured stats blending
        return STATS_BLENDER.injuredBlend(position);
    }

    public String findPrimaryExplanation(List<String> explanations) {
        return explanations.get(0);
    }

    public List<String> findSupportingExplanations(List<String> explanations) {
        List<String> supportingExplanations = new LinkedList<>();
        supportingExplanations.add(explanations.get(1));
        supportingExplanations.add(explanations.get(2));
        return supportingExplanations;
    }

    /**
     * Applies a hyperbolic soft cap to raw scores exceeding the elite threshold (85.0).
     * <p>
     * This ensures that while elite performances are rewarded, the final
     * Scare Factor remains realistically bound below 100.0.
     *
     * @param rawScore The un-capped calculated impact score.
     * @return A score between 0.0 and 99.9.
     */
    protected double applySoftCap(double rawScore) {
        if (rawScore <= 85.0) {
            return rawScore;
        }

        double excess = rawScore - 85.0;
        double range = 15.0;

        double cappedScore = 85.0 + (excess / (1.0 + (excess / range)));

        return Math.min(99.9, cappedScore);
    }

    /**
     * Generates a mapping of player statistics to their relative "Impact" (points gained/lost).
     * This is implemented uniquely by each position subclass to reflect different scoring values.
     *
     * @return A map of stats to Impact objects.
     */
    protected abstract Map<PlayerStats, Impact> generateImpactMap();

    /**
     * Calculates the points gained or lost for a specific statistic based on its
     * performance ratio (actual vs. league max) and its weight.
     *
     * @param ratio  The normalized performance (0.0 to 1.0+).
     * @param weight The importance of this stat for the player's position.
     * @return An Impact object containing pointsGained and pointsLost.
     */
    protected Impact generateImpactForStat(double ratio, double weight) {
        double pointsGained = ratio * weight;
        double pointsLost = (1.0 - ratio) * weight;
        return new Impact(pointsGained, pointsLost);
    }

    /**
     * Identifies the top 3 statistical contributors (positive or negative) to a player's score.
     *
     * @param map The map of statistical impacts.
     * @return A sorted map containing the top 3 most significant statistical factors.
     */
    protected Map<PlayerStats, Double> findTopContributingScores(Map<PlayerStats, Impact> map) {
        // ...
        Map<PlayerStats, Double> initialMap = new HashMap<>();

        for (Map.Entry<PlayerStats, Impact> entry : map.entrySet()) {
            double pointsGained = entry.getValue().pointsGained;
            double pointsLost = entry.getValue().pointsLost;

            if (pointsGained >= pointsLost) {
                initialMap.put(entry.getKey(), pointsGained);
            } else {
                initialMap.put(entry.getKey(), pointsLost * -1);
            }
        }

        List<Map.Entry<PlayerStats, Double>> entries = new ArrayList<>(initialMap.entrySet());

        entries.sort((a, b) -> Double.compare(Math.abs(b.getValue()), Math.abs(a.getValue())));

        Map<PlayerStats, Double> result = new LinkedHashMap<>();
        for (Map.Entry<PlayerStats, Double> entry : entries) {
            if (result.size() >= 3) break;
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    protected int getTierForStatistic(double value, double weight) {
        if (value > 0) {
            if (value > (weight * 0.9)) {
                return 1;
            } else if (value > (weight * 0.6)) {
                return 2;
            } else {
                return 3;
            }
        } else {
            if (Math.abs(value) > (weight * 0.9)) {
                return -1;
            } else if (Math.abs(value) > (weight * 0.6)) {
                return -2;
            } else {
                return -3;
            }
        }
    }

    @Getter
    protected static class Impact {
        private final double pointsGained;
        private final double pointsLost;

        public Impact(double pointsGained, double pointsLost) {
            this.pointsGained = pointsGained;
            this.pointsLost = pointsLost;
        }
    }

}
