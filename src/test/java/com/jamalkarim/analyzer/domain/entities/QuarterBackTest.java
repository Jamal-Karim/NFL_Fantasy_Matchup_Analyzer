package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.Stats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
