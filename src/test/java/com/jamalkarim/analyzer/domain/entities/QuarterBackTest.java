package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.Stats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuarterBackTest {

    @Test
    void testQuarterbackConstructor() {
        String name = "Josh Allen";
        String team = "Buffalo Bills";

        QuarterBack quarterback = new QuarterBack(name, team);

        assertEquals(name, quarterback.getName(), "The player's name should match the name provided in the constructor.");
        assertEquals(team, quarterback.getTeam(), "The player's team should match the team provided in the constructor.");
        assertEquals(Position.QB, quarterback.getPosition(), "The player's position should be set to 'QB'.");
    }

    @Test
    void testCalculateScareFactor() {
        QuarterBack qb = new QuarterBack("Josh Allen", "Bills");
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setPassingYards(300);
        stats.setPassingTDs(2);
        stats.setInterceptions(0);
        stats.setRushingYards(50);
        stats.setRushingTDs(1);
        qb.setCurrentSeasonStats(stats);

        double scareFactor = qb.calculateScareFactor();
        
        assertTrue(scareFactor > 85.0, "High performing QB should have a scare factor above 85.");
        assertTrue(scareFactor < 100.0, "Scare factor should be capped below 100.");
    }

    @Test
    void testCalculateScareFactorZeroStats() {
        QuarterBack qb = new QuarterBack("Zero Man", "None");
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        qb.setCurrentSeasonStats(stats);

        double scareFactor = qb.calculateScareFactor();
        assertEquals(0.0, scareFactor, "Player with zero stats should have a scare factor of 0.");
    }

    @Test
    void testCalculateScareFactorPoorPerformance() {
        QuarterBack qb = new QuarterBack("Mistake Prone", "Bad");
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setInterceptions(5);
        qb.setCurrentSeasonStats(stats);

        double scareFactor = qb.calculateScareFactor();
        assertEquals(0.0, scareFactor, "Negative impacts should be clamped to 0.");
    }

    @Test
    void testPreventDivisionByZeroInCompletionPct() {
        QuarterBack qb = new QuarterBack("No Throws", "None");
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setPassAttempts(0);
        qb.setCurrentSeasonStats(stats);

        double scareFactor = qb.calculateScareFactor();
        assertEquals(0.0, scareFactor, "Player with no throws should have a scare factor of 0.");
    }

    @Test
    void testGenerateListOfExplanations() {
        QuarterBack qb = new QuarterBack("Elite QB", "Team");
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setPassingYards(300);
        stats.setPassingTDs(3);
        qb.setCurrentSeasonStats(stats);

        java.util.List<String> explanations = qb.generateListOfExplanations();
        assertFalse(explanations.isEmpty(), "Explanations list should not be empty for an elite player.");
        assertTrue(explanations.get(0).contains("Elite") || explanations.get(0).contains("Highly productive"), 
            "First explanation should reflect high performance.");
    }
}
