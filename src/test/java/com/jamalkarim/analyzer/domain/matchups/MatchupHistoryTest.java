package com.jamalkarim.analyzer.domain.matchups;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class MatchupHistoryTest {

    private MatchupHistory history;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        history = new MatchupHistory();
        System.setOut(new PrintStream(outputStream));
    }

    private TeamMatchupResult createMockResult(String team1, String team2, double score1, double score2) {
        TeamMatchupResult result = new TeamMatchupResult(team1, team2);
        result.setTeam1TotalScore(score1);
        result.setTeam2TotalScore(score2);
        return result;
    }

    @Test
    @DisplayName("Should maintain a maximum of 5 matchups")
    void shouldMaintainMaxSize() {
        for (int i = 1; i <= 7; i++) {
            history.addMatchup(createMockResult("Team" + i + "A", "Team" + i + "B", 100, 90));
        }

        assertThat(history.getHistorySize())
                .as("History should never exceed 5 items")
                .isEqualTo(5);
    }

    @Test
    @DisplayName("Should correctly identify winner and handle draws in display")
    void shouldHandleWinnerAndDraws() {
        history.addMatchup(createMockResult("Eagles", "Cowboys", 110, 100)); // Eagles win
        history.addMatchup(createMockResult("Niners", "Rams", 80, 80));      // Draw
        history.addMatchup(createMockResult("Bills", "Chiefs", 90, 105));    // Chiefs win

        history.displayHistory();
        String output = outputStream.toString();

        assertThat(output)
                .as("Should identify Eagles as winner")
                .contains("Winner: Eagles")
                .as("Should identify Chiefs as winner")
                .contains("Winner: Chiefs")
                .as("Should identify Draw when scores are equal")
                .contains("Winner: Draw");
    }

    @Test
    @DisplayName("Should display history in reverse chronological order")
    void shouldDisplayInCorrectOrder() {
        history.addMatchup(createMockResult("T1", "T1B", 100, 90));
        history.addMatchup(createMockResult("T2", "T2B", 100, 90));

        history.displayHistory();
        String output = outputStream.toString();

        int indexT1 = output.indexOf("T1 vs T1B");
        int indexT2 = output.indexOf("T2 vs T2B");

        assertThat(indexT2)
                .as("Most recent matchup (T2) should appear first")
                .isLessThan(indexT1);
    }
}
