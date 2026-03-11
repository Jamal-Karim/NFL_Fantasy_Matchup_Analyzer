package com.jamalkarim.analyzer.domain.matchups;

import com.jamalkarim.analyzer.domain.entities.QuarterBack;
import com.jamalkarim.analyzer.domain.enums.MatchupAdvantages;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMatchupResultTest {

    @Test
    void testMatchupResultInitialization() {
        QuarterBack qb1 = new QuarterBack("QB1", "Team1");
        QuarterBack qb2 = new QuarterBack("QB2", "Team2");
        PlayerMatchupResult result = new PlayerMatchupResult(qb1, qb2);

        assertEquals(qb1, result.getPlayer1(), "Player 1 should be correctly initialized.");
        assertEquals(qb2, result.getPlayer2(), "Player 2 should be correctly initialized.");
    }

    @Test
    void testPrintMatchupReportDoesNotThrow() {
        QuarterBack qb1 = new QuarterBack("QB1", "Team1");
        QuarterBack qb2 = new QuarterBack("QB2", "Team2");
        PlayerMatchupResult result = new PlayerMatchupResult(qb1, qb2);

        result.setWinner(qb1);
        result.setAdvantage(MatchupAdvantages.DOMINANT);
        result.setScareDifference(25.5);
        result.setExplanation("QB1 is elite");

        ScareResult r1 = new ScareResult(qb1);
        r1.setScareScore(95.0);
        ScareResult r2 = new ScareResult(qb2);
        r2.setScareScore(69.5);

        result.setPlayer1ScareResult(r1);
        result.setPlayer2ScareResult(r2);

        assertDoesNotThrow(result::printMatchupReport, "Printing the matchup report should not throw an exception.");
    }
}
