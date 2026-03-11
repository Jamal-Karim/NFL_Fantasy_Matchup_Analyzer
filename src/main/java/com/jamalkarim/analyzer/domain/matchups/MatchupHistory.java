package com.jamalkarim.analyzer.domain.matchups;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Maintains a rolling history of recent team matchup results.
 * <p>
 * This class uses a fixed-size buffer (5) to keep track of the most recent
 * analysis reports, automatically rotating out the oldest results.
 */
public class MatchupHistory {

    private static final int MAX_HISTORY_SIZE = 5;
    private Deque<TeamMatchupResult> matchupResults = new LinkedList<>();

    /**
     * Adds a new result to the history. If the history exceeds the maximum
     * allowed size, the oldest entry is removed.
     *
     * @param matchup The team matchup result to store.
     */
    public void addMatchup(TeamMatchupResult matchup) {
        matchupResults.addLast(matchup);

        if (matchupResults.size() > MAX_HISTORY_SIZE) {
            matchupResults.removeFirst();
        }
    }

    /**
     * Prints the recent matchup history to the console in reverse chronological order.
     * <p>
     * Displays the team names, total scores, and the determined winner (or "Draw").
     */
    public void displayHistory() {
        System.out.println("RECENT MATCHUP HISTORY:");
        matchupResults.descendingIterator().forEachRemaining(result -> {
            String winner;
            if (result.getTeam1TotalScore() > result.getTeam2TotalScore()) {
                winner = result.getTeam1();
            } else if (result.getTeam2TotalScore() > result.getTeam1TotalScore()) {
                winner = result.getTeam2();
            } else {
                winner = "Draw";
            }

            System.out.printf("[%s vs %s] Score: %.0f - %.0f | Winner: %s\n",
                    result.getTeam1(), result.getTeam2(),
                    result.getTeam1TotalScore(), result.getTeam2TotalScore(),
                    winner);
        });
    }

    public int getHistorySize() {
        return matchupResults.size();
    }
}
