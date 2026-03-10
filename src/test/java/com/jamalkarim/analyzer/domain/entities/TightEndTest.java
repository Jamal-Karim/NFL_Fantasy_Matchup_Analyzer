package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.Stats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TightEndTest {

    @Test
    void testTightEndConstructor() {
        String name = "Travis Kelce";
        String team = "Kansas City Chiefs";

        TightEnd tightEnd = new TightEnd(name, team);

        assertEquals(name, tightEnd.getName(), "The player's name should match the name provided in the constructor.");
        assertEquals(team, tightEnd.getTeam(), "The player's team should match the team provided in the constructor.");
        assertEquals(Position.TE, tightEnd.getPosition(), "The player's position should be set to 'TE'.");
    }

    @Test
    void testCalculateScareFactor() {
        TightEnd te = new TightEnd("Kelce", "Chiefs");
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setReceptions(7);
        stats.setReceivingYards(80);
        stats.setReceivingTDs(1);
        te.setCurrentSeasonStats(stats);

        double scareFactor = te.calculateScareFactor();

        assertTrue(scareFactor > 85.0, "High performing TE should have a scare factor above 85.");
        assertTrue(scareFactor < 100.0, "Scare factor should be capped below 100.");
    }

    @Test
    void testTightEndReceptionBonus() {
        TightEnd te = new TightEnd("Bonus TE", "Team");
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setReceptions(7);
        te.setCurrentSeasonStats(stats);

        double scoreWithBonus = te.calculateScareFactor();

        stats.setReceptions(6);
        double scoreWithoutBonus = te.calculateScareFactor();

        assertTrue(scoreWithBonus > scoreWithoutBonus, "TE with > 6.5 receptions per game should get a bonus.");
    }
}

