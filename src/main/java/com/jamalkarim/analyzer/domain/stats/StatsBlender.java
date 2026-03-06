package com.jamalkarim.analyzer.domain.stats;

import com.jamalkarim.analyzer.domain.enums.Position;

public class StatsBlender {

    private static final int GAMES_IN_SEASON = 17;

    // Blended stats for a player who played last season and upcoming / current season
    public BlendedStats standardBlend(Stats lastSeasonStats, Stats currentSeasonStats){

        int gamesPlayedInCurrentSeason = currentSeasonStats.getGamesPlayed();
        int gamesPlayedInLastSeason = lastSeasonStats.getGamesPlayed();

        double gameRatio = (double) gamesPlayedInCurrentSeason / GAMES_IN_SEASON;
        double currentSeasonWeight = Math.sqrt(gameRatio);
        double lastSeasonWeight = 1 - currentSeasonWeight;

        StatsBlenderHelper helper = new StatsBlenderHelper(gamesPlayedInCurrentSeason, gamesPlayedInLastSeason,
                currentSeasonWeight, lastSeasonWeight);

        return mapStats(lastSeasonStats, currentSeasonStats, helper);
    }

    // Blended stats for a player who played last season and has 0 games in the current season
    // Or for a player who has not played any games last season and only played in the current season
    public BlendedStats singleSeasonBlend(Stats singleSeasonStats){

        int gamesPlayedInLastSeason = singleSeasonStats.getGamesPlayed();

        StatsBlenderHelper helper = new StatsBlenderHelper(0, gamesPlayedInLastSeason, 0.0, 1.0);

        return mapStats(singleSeasonStats, new Stats(), helper);
    }

    // Baseline stats for an injured player who has not played any games
    public BlendedStats injuredBlend(Position position){

        return baselineBlend(getBaseStatsByPosition(position), 1.0);
    }

    // Baseline stats for a rookie who has not played any games
    public BlendedStats rookieBlend(Position position, int draftPosition){
        double weight;

        if(draftPosition >= 1 && draftPosition <= 10){
            weight = 1.2;
        } else if(draftPosition >= 11 && draftPosition <= 25){
            weight = 0.9;
        } else{
            weight = 0.6;
        }

        return baselineBlend(getBaseStatsByPosition(position), weight);

    }

    // Baseline stats to use for rookie stats or an injured player stats
    private BlendedStats baselineBlend(Stats baseStats, double weight){
        StatsBlenderHelper helper = new StatsBlenderHelper(0, GAMES_IN_SEASON, 0.0, weight);

        return mapStats(baseStats, new Stats(), helper);
    }

    private Stats getBaseStatsByPosition(Position position) {
        return switch (position) {
            case QB -> getBaselineStatsForQB();
            case RB -> getBaselineStatsForRB();
            case WR -> getBaselineStatsForWR();
            case TE -> getBaselineStatsForTE();
        };
    }

    private BlendedStats mapStats(Stats last, Stats current, StatsBlenderHelper helper) {
        BlendedStats blended = new BlendedStats();

        // --- Passing Stats ---
        blended.setPassingYardsPerGame(helper.blend(current.getPassingYards(), last.getPassingYards()));
        blended.setPassingTDsPerGame(helper.blend(current.getPassingTDs(), last.getPassingTDs()));
        blended.setIntsPerGame(helper.blend(current.getInterceptions(), last.getInterceptions()));

        // --- Rushing Stats ---
        blended.setRushingAttemptsPerGame(helper.blend(current.getRushingAttempts(), last.getRushingAttempts()));
        blended.setRushingYardsPerGame(helper.blend(current.getRushingYards(), last.getRushingYards()));
        blended.setRushingTDsPerGame(helper.blend(current.getRushingTDs(), last.getRushingTDs()));

        // --- Receiving Stats ---
        blended.setTargetsPerGame(helper.blend(current.getTargets(), last.getTargets()));
        blended.setReceptionsPerGame(helper.blend(current.getReceptions(), last.getReceptions()));
        blended.setReceivingYardsPerGame(helper.blend(current.getReceivingYards(), last.getReceivingYards()));
        blended.setReceivingTDsPerGame(helper.blend(current.getReceivingTDs(), last.getReceivingTDs()));

        return blended;
    }

    private Stats getBaselineStatsForQB(){

        Stats baselineStats = new Stats();

        baselineStats.setPassingYards(3000);
        baselineStats.setPassingTDs(25);
        baselineStats.setInterceptions(10);

        baselineStats.setRushingAttempts(35);
        baselineStats.setRushingYards(100);
        baselineStats.setRushingTDs(1);

        return baselineStats;
    }

    private Stats getBaselineStatsForRB(){

        Stats baselineStats = new Stats();

        baselineStats.setRushingAttempts(250);
        baselineStats.setRushingYards(800);
        baselineStats.setRushingTDs(10);

        baselineStats.setTargets(40);
        baselineStats.setReceptions(30);
        baselineStats.setReceivingYards(200);
        baselineStats.setReceivingTDs(1);

        return baselineStats;
    }

    private Stats getBaselineStatsForWR(){

        Stats baselineStats = new Stats();

        baselineStats.setTargets(100);
        baselineStats.setReceptions(75);
        baselineStats.setReceivingYards(800);
        baselineStats.setReceivingTDs(5);

        return baselineStats;
    }

    private Stats getBaselineStatsForTE(){

        Stats baselineStats = new Stats();

        baselineStats.setTargets(40);
        baselineStats.setReceptions(30);
        baselineStats.setReceivingYards(250);
        baselineStats.setReceivingTDs(2);

        return baselineStats;
    }


    private static class StatsBlenderHelper{
        // current Games, last Games
        private final int cG, lG;

        // current Weight, last Weight
        private final double cW, lW;

        private StatsBlenderHelper(int cG, int lG, double cW, double lW) {
            this.cG = cG;
            this.lG = lG;
            this.cW = cW;
            this.lW = lW;
        }

        public double blend(int currentVal, int lastVal) {

            double currentPerGame = (cG > 0) ? (double) currentVal / cG : 0;
            double lastPerGame = (lG > 0) ? (double) lastVal / lG : 0;

            double result = (currentPerGame * cW) + (lastPerGame * lW);
            return Math.round(result * 100.0) / 100.0;
        }
    }
}
