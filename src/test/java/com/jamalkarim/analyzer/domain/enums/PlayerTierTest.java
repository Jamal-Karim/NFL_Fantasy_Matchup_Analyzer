package com.jamalkarim.analyzer.domain.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTierTest {

    @Test
    void testFromScore() {
        assertEquals(PlayerTier.ELITE, PlayerTier.fromScore(95.0), "Score of 95.0 should be ELITE.");
        assertEquals(PlayerTier.ELITE, PlayerTier.fromScore(90.0), "Score of 90.0 should be ELITE.");
        assertEquals(PlayerTier.SCARY, PlayerTier.fromScore(85.0), "Score of 85.0 should be SCARY.");
        assertEquals(PlayerTier.SCARY, PlayerTier.fromScore(80.0), "Score of 80.0 should be SCARY.");
        assertEquals(PlayerTier.STRONG, PlayerTier.fromScore(75.0), "Score of 75.0 should be STRONG.");
        assertEquals(PlayerTier.STRONG, PlayerTier.fromScore(70.0), "Score of 70.0 should be STRONG.");
        assertEquals(PlayerTier.AVERAGE, PlayerTier.fromScore(60.0), "Score of 60.0 should be AVERAGE.");
        assertEquals(PlayerTier.AVERAGE, PlayerTier.fromScore(50.0), "Score of 50.0 should be AVERAGE.");
        assertEquals(PlayerTier.WEAK, PlayerTier.fromScore(40.0), "Score of 40.0 should be WEAK.");
        assertEquals(PlayerTier.WEAK, PlayerTier.fromScore(0.0), "Score of 0.0 should be WEAK.");
    }

    @Test
    void testFromScoreBoundaries() {
        assertEquals(PlayerTier.SCARY, PlayerTier.fromScore(89.99), "Score of 89.99 should be SCARY.");
        assertEquals(PlayerTier.WEAK, PlayerTier.fromScore(-10.0), "Negative scores should default to WEAK.");
        assertEquals(PlayerTier.ELITE, PlayerTier.fromScore(105.0), "Scores above 101 should be ELITE.");
    }
}
