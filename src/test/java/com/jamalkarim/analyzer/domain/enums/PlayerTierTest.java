package com.jamalkarim.analyzer.domain.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTierTest {

    @Test
    void testFromScore() {
        assertEquals(PlayerTier.ELITE, PlayerTier.fromScore(95.0));
        assertEquals(PlayerTier.ELITE, PlayerTier.fromScore(90.0));
        assertEquals(PlayerTier.SCARY, PlayerTier.fromScore(85.0));
        assertEquals(PlayerTier.SCARY, PlayerTier.fromScore(80.0));
        assertEquals(PlayerTier.STRONG, PlayerTier.fromScore(75.0));
        assertEquals(PlayerTier.STRONG, PlayerTier.fromScore(70.0));
        assertEquals(PlayerTier.AVERAGE, PlayerTier.fromScore(60.0));
        assertEquals(PlayerTier.AVERAGE, PlayerTier.fromScore(50.0));
        assertEquals(PlayerTier.WEAK, PlayerTier.fromScore(40.0));
        assertEquals(PlayerTier.WEAK, PlayerTier.fromScore(0.0));
    }

    @Test
    void testFromScoreBoundaries() {
        assertEquals(PlayerTier.SCARY, PlayerTier.fromScore(89.99));
        assertEquals(PlayerTier.WEAK, PlayerTier.fromScore(-10.0), "Negative scores should default to WEAK.");
        assertEquals(PlayerTier.ELITE, PlayerTier.fromScore(105.0), "Scores above 101 should be ELITE.");
    }
}
