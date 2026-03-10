package com.jamalkarim.analyzer.domain.stats;

import com.jamalkarim.analyzer.domain.entities.Player;
import com.jamalkarim.analyzer.domain.entities.QuarterBack;
import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScareResultFactoryTest {

    @Test
    void testGenerateScareResult() {
        ScareResultFactory factory = new ScareResultFactory();
        Player player = new QuarterBack("Josh Allen", "BUF");
        
        Stats stats = new Stats();
        stats.setGamesPlayed(1);
        stats.setPassingYards(300);
        stats.setPassingTDs(2);
        player.setCurrentSeasonStats(stats);

        ScareResult result = factory.generateScareResult(player);

        assertNotNull(result);
        assertEquals("Josh Allen", result.getName());
        assertEquals("BUF", result.getTeam());
        assertTrue(result.getScareScore() > 0);
        assertNotNull(result.getScareTier());
        assertNotNull(result.getPrimaryExplanation());
        assertNotNull(result.getSupportingExplanations());
        assertEquals(2, result.getSupportingExplanations().size());
    }
}
