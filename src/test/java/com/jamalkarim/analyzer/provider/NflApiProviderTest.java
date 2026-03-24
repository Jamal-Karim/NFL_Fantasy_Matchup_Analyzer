package com.jamalkarim.analyzer.provider;

import com.jamalkarim.analyzer.domain.models.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NflApiProviderTest {

    @Autowired
    private NflApiProvider nflApiProvider;

    @Test
    void testFetchPlayer_Success() {
        // 1. Act: Call the provider using your "Nabers" logic
        // (Even if you pass "Malik Nabers", your provider is currently hardcoded to your JSON files)
        Player player = nflApiProvider.fetchPlayer("Malik Nabers", "NYG");

        // 2. Assert: Verify the "Identity"
        assertNotNull(player);
        assertEquals("Malik Nabers", player.getName());
        assertEquals("WR", player.getPosition().toString());

        // 3. Assert: Verify the "Refinery" (Stats & Status)
        // Ensure your mapper pulled "271" from the deep categories list
        assertNotNull(player.getCurrentSeasonStats());
        assertEquals(271, player.getCurrentSeasonStats().getReceivingYards());

        // Ensure your mapper turned the "injuries" list into a boolean
        assertTrue(player.isInjured());

        // Ensure your mapper parsed the "displayDraft" string for the rookie flag
        assertFalse(player.isRookie());

        System.out.println("Test Passed: Player Domain Object fully hydrated from 3 JSON files.");
    }
}