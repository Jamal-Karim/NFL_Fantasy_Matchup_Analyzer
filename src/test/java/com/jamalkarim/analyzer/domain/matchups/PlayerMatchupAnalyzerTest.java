package com.jamalkarim.analyzer.domain.matchups;

import com.jamalkarim.analyzer.domain.entities.QuarterBack;
import com.jamalkarim.analyzer.domain.enums.PlayerAdvantages;
import com.jamalkarim.analyzer.domain.stats.Stats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMatchupAnalyzerTest {

    private PlayerMatchupAnalyzer analyzer;
    private QuarterBack qb1;
    private QuarterBack qb2;

    @BeforeEach
    void setUp() {
        analyzer = new PlayerMatchupAnalyzer();
        qb1 = new QuarterBack("QB One", "Team A");
        qb2 = new QuarterBack("QB Two", "Team B");
    }

    @Test
    void testAnalyzeMatchupWinnerPlayer1() {
        Stats stats1 = new Stats();
        stats1.setGamesPlayed(1);
        stats1.setPassingYards(350);
        stats1.setPassingTDs(4);
        qb1.setCurrentSeasonStats(stats1);

        Stats stats2 = new Stats();
        stats2.setGamesPlayed(1);
        PlayerMatchupResult result = analyzer.analyzePlayerMatchup(qb1, qb2);

        assertEquals(qb1, result.getWinner().orElse(null), "Player 1 should be the winner.");
        assertEquals(qb2, result.getLoser().orElse(null), "Player 2 should be the loser.");
        assertTrue(result.getScareDifference() > 15, "The scare difference should be greater than 15.");
        assertEquals(PlayerAdvantages.DOMINANT, result.getAdvantage(), "The advantage should be DOMINANT.");
        }

        @Test
        void testAnalyzeMatchupWinnerPlayer2() {
        // qb1 is average
        Stats stats1 = new Stats();
        stats1.setGamesPlayed(1);
        stats1.setPassingYards(150);
        qb1.setCurrentSeasonStats(stats1);

        // qb2 is slightly better
        Stats stats2 = new Stats();
        stats2.setGamesPlayed(1);
        stats2.setPassingYards(200);
        qb2.setCurrentSeasonStats(stats2);

        PlayerMatchupResult result = analyzer.analyzePlayerMatchup(qb1, qb2);

        assertEquals(qb2, result.getWinner().orElse(null), "Player 2 should be the winner.");
        assertEquals(PlayerAdvantages.SLIGHT_EDGE, result.getAdvantage(), "The advantage should be SLIGHT_EDGE.");
        }

        @Test
        void testAnalyzeMatchupDeadHeat() {
        // Both have zero stats
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        qb1.setCurrentSeasonStats(stats);
        qb2.setCurrentSeasonStats(stats);

        PlayerMatchupResult result = analyzer.analyzePlayerMatchup(qb1, qb2);

        assertFalse(result.getWinner().isPresent(), "There should be no winner in a dead heat.");
        assertEquals(PlayerAdvantages.EVEN, result.getAdvantage(), "The advantage should be EVEN in a dead heat.");
        assertEquals("Both players bring equal threat levels.", result.getExplanation(), "The explanation should indicate equal threat levels.");
        }

        @Test
        void testAdvantageThresholds() {
        PlayerMatchupResult result = analyzer.analyzePlayerMatchup(qb1, qb2);
        assertNotNull(result.getAdvantage(), "The advantage should not be null.");
        }
        }

