package com.jamalkarim.analyzer.domain.entities;

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
}
