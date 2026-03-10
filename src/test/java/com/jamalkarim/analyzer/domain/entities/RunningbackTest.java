package com.jamalkarim.analyzer.domain.entities;

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
}
