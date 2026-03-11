package com.jamalkarim.analyzer.domain.matchups;

import com.jamalkarim.analyzer.domain.entities.Player;
import com.jamalkarim.analyzer.domain.enums.MatchupAdvantages;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import lombok.Data;

import java.util.Optional;

/**
 * Represents the final report of a head-to-head player comparison.
 * Contains winner/loser data, the magnitude of the advantage,
 * and an explanation of the outcome.
 */
@Data
public class PlayerMatchupResult {
    private Player player1;
    private Player player2;
    private double scareDifference;
    private MatchupAdvantages advantage;
    private Player winner;
    private Player loser;
    private String explanation;
    private ScareResult player1ScareResult;
    private ScareResult player2ScareResult;

    public PlayerMatchupResult(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * @return An Optional containing the winner, or empty if the matchup resulted in a tie.
     */
    public Optional<Player> getWinner() {
        return Optional.ofNullable(winner);
    }

    /**
     * @return An Optional containing the loser, or empty if the matchup resulted in a tie.
     */
    public Optional<Player> getLoser() {
        return Optional.ofNullable(loser);
    }

    public void printMatchupReport() {
        String separator = "==========================================";
        String subSeparator = "------------------------------------------";

        System.out.println("\n" + separator);
        System.out.println("MATCHUP: " + player1.getName() + " vs. " + player2.getName());
        System.out.println(separator);

        if (winner != null) {
            System.out.println("WINNER: " + winner.getName().toUpperCase());
            System.out.println("ADVANTAGE: " + advantage + " (Diff: " + String.format("%.1f", scareDifference) + ")");
        } else {
            System.out.println("RESULT: DEAD HEAT");
            System.out.println("ADVANTAGE: " + advantage);
        }

        System.out.println(subSeparator);
        System.out.println("ANALYSIS:");
        System.out.println(explanation);
        System.out.println(subSeparator);

        System.out.println("SCORES:");
        System.out.println(player1.getName() + ": " + (int) player1ScareResult.getScareScore() + " [" + player1ScareResult.getScareTier() + "]");
        System.out.println(player2.getName() + ": " + (int) player2ScareResult.getScareScore() + " [" + player2ScareResult.getScareTier() + "]");

        System.out.println(separator + "\n");
    }
}
