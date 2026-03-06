package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TightEndTest {

    @Test
    void testTightEndConstructor() {
        String name = "Brock Bowers";
        String team = "Las Vegas Raiders";

        TightEnd tightEnd = new TightEnd(name, team);

        assertEquals(name, tightEnd.getName(), "The player's name should match the name provided in the constructor.");
        assertEquals(team, tightEnd.getTeam(), "The player's team should match the team provided in the constructor.");
        assertEquals(Position.TE, tightEnd.getPosition(), "The player's position should be set to 'TE'.");
    }
}
