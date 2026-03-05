package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RunningbackTest {

    @Test
    void testRunningbackConstructor() {
        // Given
        String name = "Christian McCaffrey";
        String team = "San Francisco 49ers";
        int rushingAttempts = 300;
        int rushingYards = 1200;
        int receptions = 80;
        int receivingYards = 600;
        int rushingTouchdowns = 12;
        int receivingTouchdowns = 5;

        // When
        Runningback runningback = new Runningback(name, team, rushingAttempts, rushingYards,
                receptions, receivingYards, rushingTouchdowns, receivingTouchdowns);

        // Then
        assertEquals(name, runningback.getName());
        assertEquals(team, runningback.getTeam());
        assertEquals(Position.RB, runningback.getPosition());
    }
}
