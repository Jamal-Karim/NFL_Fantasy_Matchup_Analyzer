package com.jamalkarim.analyzer.domain.stats;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.models.QuarterBack;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.domain.scoring.ScareResultFactory;
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

        assertNotNull(result, "The generated ScareResult should not be null.");
        assertEquals("Josh Allen", result.getName(), "The player name in the result should match.");
        assertEquals("BUF", result.getTeam(), "The team in the result should match.");
        assertTrue(result.getScareScore() > 0, "The scare score should be greater than 0.");
        assertNotNull(result.getScareTier(), "The scare tier should not be null.");
        assertNotNull(result.getPrimaryExplanation(), "The primary explanation should not be null.");
        assertNotNull(result.getSupportingExplanations(), "The supporting explanations should not be null.");
        assertEquals(2, result.getSupportingExplanations().size(), "There should be exactly 2 supporting explanations.");
    }
}
