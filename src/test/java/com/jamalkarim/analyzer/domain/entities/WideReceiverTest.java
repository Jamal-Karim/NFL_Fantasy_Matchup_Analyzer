package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WideReceiverTest {

    @Test
    void testWideReceiverConstructor() {
        String name = "Justin Jefferson";
        String team = "Minnesota Vikings";
        int receptions = 110;
        int receivingYards = 1700;
        int receivingTouchdowns = 10;
        int rushingYards = 50;
        int rushingTouchdowns = 1;

        WideReceiver wideReceiver = new WideReceiver(name, team, rushingYards, receptions,
                receivingYards, rushingTouchdowns, receivingTouchdowns);

        assertEquals(name, wideReceiver.getName(), "The player's name should match the name provided in the constructor.");
        assertEquals(team, wideReceiver.getTeam(), "The player's team should match the team provided in the constructor.");
        assertEquals(Position.WR, wideReceiver.getPosition(), "The player's position should be set to 'WR'.");
    }
}
