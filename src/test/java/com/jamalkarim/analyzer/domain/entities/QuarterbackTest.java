package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuarterbackTest {

    @Test
    void testQuarterbackConstructor() {
        String name = "Josh Allen";
        String team = "Buffalo Bills";

        Quarterback quarterback = new Quarterback(name, team);

        assertEquals(name, quarterback.getName(), "The player's name should match the name provided in the constructor.");
        assertEquals(team, quarterback.getTeam(), "The player's team should match the team provided in the constructor.");
        assertEquals(Position.QB, quarterback.getPosition(), "The player's position should be set to 'QB'.");
    }
}
