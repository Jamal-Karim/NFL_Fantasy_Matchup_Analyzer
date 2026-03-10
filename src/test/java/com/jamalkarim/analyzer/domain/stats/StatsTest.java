package com.jamalkarim.analyzer.domain.stats;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatsTest {

    @Test
    void testStatsGettersSetters() {
        Stats stats = new Stats();
        stats.setSeason(2023);
        stats.setGamesPlayed(17);
        stats.setPassingYards(4000);

        assertEquals(2023, stats.getSeason(), "Season should be correctly set and retrieved.");
        assertEquals(17, stats.getGamesPlayed(), "Games played should be correctly set and retrieved.");
        assertEquals(4000, stats.getPassingYards(), "Passing yards should be correctly set and retrieved.");
    }

    @Test
    void testBlendedStatsGettersSetters() {
        BlendedStats blended = new BlendedStats();
        blended.setPassingYardsPerGame(250.5);
        blended.setRushingTDsPerGame(0.5);

        assertEquals(250.5, blended.getPassingYardsPerGame(), "Passing yards per game should be correctly set and retrieved.");
        assertEquals(0.5, blended.getRushingTDsPerGame(), "Rushing TDs per game should be correctly set and retrieved.");
    }
}
