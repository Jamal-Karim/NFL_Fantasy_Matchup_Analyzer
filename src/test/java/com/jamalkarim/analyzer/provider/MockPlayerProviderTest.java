package com.jamalkarim.analyzer.provider;

import com.jamalkarim.analyzer.domain.models.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class MockPlayerProviderTest {

    @Autowired
    private MockPlayerProvider provider;

    @Test
    void fetchPlayer_Success() {
        Player player = provider.fetchPlayer("Patrick Mahomes", "KC");
        assertNotNull(player);
        assertEquals("Patrick Mahomes", player.getName());
        assertEquals("KC", player.getTeam());
    }

    @Test
    void fetchPlayer_NotFound() {
        Player player = provider.fetchPlayer("NonExistent", "NO");
        assertNull(player);
    }
}
