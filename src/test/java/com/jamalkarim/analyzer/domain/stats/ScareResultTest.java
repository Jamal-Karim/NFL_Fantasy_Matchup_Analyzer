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

        assertEquals("Test Player", result.getName(), "The name should be correctly initialized.");
        assertEquals("Test Team", result.getTeam(), "The team should be correctly initialized.");
        assertEquals(Position.QB, result.getPosition(), "The position should be correctly initialized.");
    }

    @Test
    void testPrintScareReportDoesNotThrow() {
        QuarterBack qb = new QuarterBack("Test Player", "Test Team");
        ScareResult result = new ScareResult(qb);
        result.setScareScore(85.5);
        result.setScareTier(PlayerTier.SCARY);
        result.setPrimaryExplanation("High yardage output");
        result.setSupportingExplanations(List.of("Reliable hands", "Great vision"));

        assertDoesNotThrow(result::printScareReport, "Printing the scare report should not throw an exception.");
    }
}
