package com.jamalkarim.analyzer.domain.stats;

public class StatsBlender {

    public Stats blend(Stats lastSeasonStats, Stats currentSeasonStats){
        return currentSeasonStats;
    }

    public Stats blend(Stats currentSeasonStats){
        return currentSeasonStats;
    }
}
