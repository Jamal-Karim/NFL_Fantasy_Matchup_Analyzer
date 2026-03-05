package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void testPlayerConstructor(){
        Player player = new Player("NFL Player", "NFL Team", Position.QB) {
            @Override
            public double calculateScareFactor() {
                return 0;
            }
        };

        assertEquals("NFL Player", player.getName(), "The player's name should match the name provided in the constructor.");
        assertEquals("NFL Team", player.getTeam(), "The player's team should match the team provided in the constructor.");
    }

}
