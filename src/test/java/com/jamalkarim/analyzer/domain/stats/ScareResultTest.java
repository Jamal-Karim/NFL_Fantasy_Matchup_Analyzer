package com.jamalkarim.analyzer.domain.stats;

import com.jamalkarim.analyzer.domain.entities.QuarterBack;
import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScareResultTest {

    @Test
    void testScareResultInitialization() {
        QuarterBack qb = new QuarterBack("Test Player", "Test Team");
        ScareResult result = new ScareResult(qb);

        assertEquals("Test Player", result.getName());
        assertEquals("Test Team", result.getTeam());
        assertEquals(Position.QB, result.getPosition());
    }

    @Test
    void testPrintScareReportDoesNotThrow() {
        QuarterBack qb = new QuarterBack("Test Player", "Test Team");
        ScareResult result = new ScareResult(qb);
        result.setScareScore(85.5);
        result.setScareTier(PlayerTier.SCARY);
        result.setPrimaryExplanation("High yardage output");
        result.setSupportingExplanations(List.of("Reliable hands", "Great vision"));

        assertDoesNotThrow(result::printScareReport);
    }
}
