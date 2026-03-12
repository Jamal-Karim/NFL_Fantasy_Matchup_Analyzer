package com.jamalkarim.analyzer.domain.models;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.Stats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RunningbackTest {

    @Test
    void testRunningbackConstructor() {
        String name = "Christian McCaffrey";
        String team = "San Francisco 49ers";

        RunningBack runningback = new RunningBack(name, team);

        assertEquals(name, runningback.getName(), "The player's name should match the name provided in the constructor.");
        assertEquals(team, runningback.getTeam(), "The player's team should match the team provided in the constructor.");
        assertEquals(Position.RB, runningback.getPosition(), "The player's position should be set to 'RB'.");
    }

    @Test
    void testCalculateScareFactor() {
        RunningBack rb = new RunningBack("CMC", "49ers");
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setRushingYards(100);
        stats.setRushingTDs(1);
        stats.setReceptions(5);
        stats.setReceivingYards(40);
        stats.setReceivingTDs(0);
        rb.setCurrentSeasonStats(stats);

        double scareFactor = rb.calculateScareFactor();

        assertTrue(scareFactor > 85.0, "High performing RB should have a scare factor above 85.");
        assertTrue(scareFactor < 100.0, "Scare factor should be capped below 100.");
    }

    @Test
    void testCalculateScareFactorZeroStats() {
        RunningBack rb = new RunningBack("Zero RB", "None");
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        rb.setCurrentSeasonStats(stats);

        double scareFactor = rb.calculateScareFactor();
        assertEquals(0.0, scareFactor, "Player with zero stats should have a scare factor of 0.");
    }

    @Test
    void testCalculateScareFactorPoorPerformance() {
        RunningBack rb = new RunningBack("Fumble Prone", "Bad");
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setRushingYards(-10);
        rb.setCurrentSeasonStats(stats);

        double scareFactor = rb.calculateScareFactor();
        assertEquals(0.0, scareFactor, "Negative impacts should be clamped to 0.");
    }
}
