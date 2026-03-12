package com.jamalkarim.analyzer.domain.models;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.Stats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WideReceiverTest {

    @Test
    void testWideReceiverConstructor() {
        String name = "Justin Jefferson";
        String team = "Minnesota Vikings";

        WideReceiver wideReceiver = new WideReceiver(name, team);

        assertEquals(name, wideReceiver.getName(), "The player's name should match the name provided in the constructor.");
        assertEquals(team, wideReceiver.getTeam(), "The player's team should match the team provided in the constructor.");
        assertEquals(Position.WR, wideReceiver.getPosition(), "The player's position should be set to 'WR'.");
    }

    @Test
    void testCalculateScareFactor() {
        WideReceiver wr = new WideReceiver("Jefferson", "Vikings");
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setReceptions(10);
        stats.setReceivingYards(150);
        stats.setReceivingTDs(1);
        wr.setCurrentSeasonStats(stats);

        double scareFactor = wr.calculateScareFactor();

        assertTrue(scareFactor > 85.0, "High performing WR should have a scare factor above 85.");
        assertTrue(scareFactor < 100.0, "Scare factor should be capped below 100.");
    }

    @Test
    void testCalculateScareFactorZeroStats() {
        WideReceiver wr = new WideReceiver("Zero WR", "None");
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        wr.setCurrentSeasonStats(stats);

        double scareFactor = wr.calculateScareFactor();
        assertEquals(0.0, scareFactor, "Player with zero stats should have a scare factor of 0.");
    }

    @Test
    void testCalculateScareFactorPoorPerformance() {
        WideReceiver wr = new WideReceiver("Hands of Stone", "Bad");
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setReceivingYards(-5);
        wr.setCurrentSeasonStats(stats);

        double scareFactor = wr.calculateScareFactor();
        assertEquals(0.0, scareFactor, "Negative impacts should be clamped to 0.");
    }
}
