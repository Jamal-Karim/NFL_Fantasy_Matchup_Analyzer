package com.jamalkarim.analyzer.domain.stats;

import com.jamalkarim.analyzer.domain.enums.Position;

/**
 * Responsible for normalizing and merging statistics from multiple sources
 * (Current Season, Last Season, and League Baselines) into a single
 * per-game representation.
 */
public class StatsBlender {

    private static final int GAMES_IN_SEASON = 17;

    /**
     * Blends current and previous season stats.
     * <p>
     * The weight of the current season is calculated as the square root of
     * the season completion percentage, allowing "momentum" to be captured
     * quickly early in the year without being overly reactive to small samples.
     *
     * @param lastSeasonStats    Stats from the previous last season
     * @param currentSeasonStats Stats from the current season (can be >= 1 game played)
     * @return a combined blend of per game stats weighting both last season and current season
     */
    public BlendedStats standardBlend(Stats lastSeasonStats, Stats currentSeasonStats) {
        // ...

        int gamesPlayedInCurrentSeason = currentSeasonStats.getGamesPlayed();
        int gamesPlayedInLastSeason = lastSeasonStats.getGamesPlayed();

        double gameRatio = (double) gamesPlayedInCurrentSeason / GAMES_IN_SEASON;
        double currentSeasonWeight = Math.sqrt(gameRatio);
        double lastSeasonWeight = 1 - currentSeasonWeight;

        StatsBlenderHelper helper = new StatsBlenderHelper(gamesPlayedInCurrentSeason, gamesPlayedInLastSeason,
                currentSeasonWeight, lastSeasonWeight);

        return mapStats(lastSeasonStats, currentSeasonStats, helper);
    }

    /**
     * Normalizes statistics using only a single season of data.
     * <p>
     * This is used when a player only has data for one relevant season (e.g.,
     * before a new season starts, or for a rookie who has played some games).
     *
     * @param singleSeasonStats The stats from the single available season
     * @return A per-game representation based on the provided season
     */
    public BlendedStats singleSeasonBlend(Stats singleSeasonStats) {

        int gamesPlayedInLastSeason = singleSeasonStats.getGamesPlayed();

        StatsBlenderHelper helper = new StatsBlenderHelper(0, gamesPlayedInLastSeason, 0.0, 1.0);

        return mapStats(singleSeasonStats, null, helper);
    }

    /**
     * Provides a baseline statistical representation for an injured player.
     * <p>
     * Returns standard league-average baselines for the player's position
     * since no recent performance data is available.
     *
     * @param position The position of the injured player
     * @return A per-game representation using position baselines
     */
    public BlendedStats injuredBlend(Position position) {

        return baselineBlend(getBaseStatsByPosition(position), 1.0);
    }

    /**
     * Provides a statistical prediction for a rookie player.
     * <p>
     * Uses position baselines weighted by the player's draft position. Higher draft
     * picks are assigned a higher statistical expectation.
     *
     * @param position      The position of the rookie player
     * @param draftPosition The overall draft pick number (1-250+)
     * @return A per-game representation weighted by draft capital
     */
    public BlendedStats rookieBlend(Position position, int draftPosition) {

        double weight = getDraftWeight(draftPosition);

        return baselineBlend(getBaseStatsByPosition(position), weight);

    }

    private double getDraftWeight(int draftPosition) {
        if (draftPosition <= 10) return 1.2;
        if (draftPosition <= 25) return 0.9;
        return 0.6;
    }

    // Baseline stats to use for rookie stats or an injured player stats
    private BlendedStats baselineBlend(Stats baseStats, double weight) {
        StatsBlenderHelper helper = new StatsBlenderHelper(0, GAMES_IN_SEASON, 0.0, weight);

        return mapStats(baseStats, null, helper);
    }

    private Stats getBaseStatsByPosition(Position position) {
        return switch (position) {
            case QB -> getBaselineStatsForQB();
            case RB -> getBaselineStatsForRB();
            case WR -> getBaselineStatsForWR();
            case TE -> getBaselineStatsForTE();
        };
    }

    // Method to blend together all stats from a season for a given player
    private BlendedStats mapStats(Stats last, Stats current, StatsBlenderHelper helper) {

        if (last == null) last = new Stats();
        if (current == null) current = new Stats();

        BlendedStats blended = new BlendedStats();

        blendPassingStats(current, last, helper, blended);
        blendRushingStats(current, last, helper, blended);
        blendReceivingStats(current, last, helper, blended);

        return blended;
    }

    private void blendPassingStats(Stats current, Stats last, StatsBlenderHelper helper, BlendedStats blended) {
        blended.setPassingYardsPerGame(helper.blend(current.getPassingYards(), last.getPassingYards()));
        blended.setPassingTDsPerGame(helper.blend(current.getPassingTDs(), last.getPassingTDs()));
        blended.setIntsPerGame(helper.blend(current.getInterceptions(), last.getInterceptions()));
    }

    private void blendRushingStats(Stats current, Stats last, StatsBlenderHelper helper, BlendedStats blended) {
        blended.setRushingAttemptsPerGame(helper.blend(current.getRushingAttempts(), last.getRushingAttempts()));
        blended.setRushingYardsPerGame(helper.blend(current.getRushingYards(), last.getRushingYards()));
        blended.setRushingTDsPerGame(helper.blend(current.getRushingTDs(), last.getRushingTDs()));
    }

    private void blendReceivingStats(Stats current, Stats last, StatsBlenderHelper helper, BlendedStats blended) {
        blended.setReceptionsPerGame(helper.blend(current.getReceptions(), last.getReceptions()));
        blended.setReceivingYardsPerGame(helper.blend(current.getReceivingYards(), last.getReceivingYards()));
        blended.setReceivingTDsPerGame(helper.blend(current.getReceivingTDs(), last.getReceivingTDs()));
    }

    private Stats getBaselineStatsForQB() {

        Stats baselineStats = new Stats();

        baselineStats.setPassingYards(3000);
        baselineStats.setPassingTDs(25);
        baselineStats.setInterceptions(10);

        baselineStats.setRushingAttempts(35);
        baselineStats.setRushingYards(100);
        baselineStats.setRushingTDs(1);

        return baselineStats;
    }

    private Stats getBaselineStatsForRB() {

        Stats baselineStats = new Stats();

        baselineStats.setRushingAttempts(250);
        baselineStats.setRushingYards(800);
        baselineStats.setRushingTDs(10);

        baselineStats.setReceptions(30);
        baselineStats.setReceivingYards(200);
        baselineStats.setReceivingTDs(1);

        return baselineStats;
    }

    private Stats getBaselineStatsForWR() {

        Stats baselineStats = new Stats();

        baselineStats.setReceptions(75);
        baselineStats.setReceivingYards(800);
        baselineStats.setReceivingTDs(5);

        return baselineStats;
    }

    private Stats getBaselineStatsForTE() {

        Stats baselineStats = new Stats();

        baselineStats.setReceptions(30);
        baselineStats.setReceivingYards(250);
        baselineStats.setReceivingTDs(2);

        return baselineStats;
    }

    private static class StatsBlenderHelper {

        private final int currentGames, lastGames;
        private final double currentWeight, lastWeight;

        private StatsBlenderHelper(int currentGames, int lastGames, double currentWeight, double lastWeight) {
            this.currentGames = currentGames;
            this.lastGames = lastGames;
            this.currentWeight = currentWeight;
            this.lastWeight = lastWeight;
        }

        public double blend(int currentVal, int lastVal) {

            double currentPerGame = (currentGames > 0) ? (double) currentVal / currentGames : 0;
            double lastPerGame = (lastGames > 0) ? (double) lastVal / lastGames : 0;

            double result = (currentPerGame * currentWeight) + (lastPerGame * lastWeight);
            return Math.round(result * 100.0) / 100.0;
        }
    }
}
