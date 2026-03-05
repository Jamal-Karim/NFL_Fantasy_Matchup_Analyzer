package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RunningbackTest {

    @Test
    void testRunningbackConstructor() {
        String name = "Christian McCaffrey";
        String team = "San Francisco 49ers";

        Runningback runningback = new Runningback(name, team);

        assertEquals(name, runningback.getName(), "The player's name should match the name provided in the constructor.");
        assertEquals(team, runningback.getTeam(), "The player's team should match the team provided in the constructor.");
        assertEquals(Position.RB, runningback.getPosition(), "The player's position should be set to 'RB'.");
    }
}
