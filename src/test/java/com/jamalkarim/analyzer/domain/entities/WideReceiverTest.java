package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
