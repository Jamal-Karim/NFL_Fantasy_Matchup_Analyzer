package com.jamalkarim.analyzer.domain.stats;

import com.jamalkarim.analyzer.domain.enums.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatsBlenderTest {

    private StatsBlender statsBlender;

    @BeforeEach
    void setUp() {
        statsBlender = new StatsBlender();
    }

    @Test
    void testStandardBlend() {
        Stats lastSeason = new Stats();
        lastSeason.setGamesPlayed(10);
        lastSeason.setPassingYards(3000); // 300 per game

        Stats currentSeason = new Stats();
        currentSeason.setGamesPlayed(4);
        currentSeason.setPassingYards(1600); // 400 per game

        BlendedStats result = statsBlender.standardBlend(lastSeason, currentSeason);
        
        assertEquals(348.47, result.getPassingYardsPerGame(), 0.1, "Standard blend should correctly weight last and current season stats.");
    }

    @Test
    void testSingleSeasonBlend() {
        Stats stats = new Stats();
        stats.setGamesPlayed(10);
        stats.setPassingYards(2500); // 250 per game

        BlendedStats result = statsBlender.singleSeasonBlend(stats);

        assertEquals(250.0, result.getPassingYardsPerGame(), "Single season blend should accurately calculate per-game statistics.");
    }

    @Test
    void testInjuredBlend() {
        BlendedStats result = statsBlender.injuredBlend(Position.QB);

        assertEquals(176.47, result.getPassingYardsPerGame(), 0.01, "Injured blend should use baseline position stats.");
    }

    @Test
    void testRookieBlendTop10() {
        BlendedStats result = statsBlender.rookieBlend(Position.RB, 5);

        assertEquals(56.47, result.getRushingYardsPerGame(), 0.01, "Rookie blend for top 10 picks should use a 1.2x multiplier on baseline stats.");
    }

    @Test
    void testRookieBlendLateRound() {
        BlendedStats result = statsBlender.rookieBlend(Position.WR, 50);

        assertEquals(28.24, result.getReceivingYardsPerGame(), 0.01, "Rookie blend for late round picks should use a 0.6x multiplier on baseline stats.");
    }

    @Test
    void testRookieBlendBoundaries() {
        assertEquals(211.76, statsBlender.rookieBlend(Position.QB, 10).getPassingYardsPerGame(), 0.01, "Pick 10 should still be in the 1.2x draft weight tier.");
        assertEquals(158.82, statsBlender.rookieBlend(Position.QB, 11).getPassingYardsPerGame(), 0.01, "Pick 11 should drop to the 0.9x draft weight tier.");
        assertEquals(158.82, statsBlender.rookieBlend(Position.QB, 25).getPassingYardsPerGame(), 0.01, "Pick 25 should still be in the 0.9x draft weight tier.");
        assertEquals(105.88, statsBlender.rookieBlend(Position.QB, 26).getPassingYardsPerGame(), 0.01, "Pick 26 should drop to the 0.6x draft weight tier.");
    }
}
