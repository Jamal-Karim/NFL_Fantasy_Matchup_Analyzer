package com.jamalkarim.analyzer.domain.matchups;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.enums.MatchupAdvantages;
import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.domain.scoring.ScareResultFactory;

/**
 * Service responsible for comparing two players and determining
 * which one presents a higher statistical threat based on Scare Factor.
 */
public class PlayerMatchupAnalyzer {

    /**
     * Performs a side-by-side comparison of two players.
     *
     * @param player1 The first player to analyze.
     * @param player2 The second player to analyze.
     * @return A PlayerMatchupResult containing the winner, advantage level, and detailed analysis.
     */
    public PlayerMatchupResult analyzePlayerMatchup(Player player1, Player player2) {
        ScareResultFactory scareResultFactory = new ScareResultFactory();
        ScareResult player1ScareResult = scareResultFactory.generateScareResult(player1);
        ScareResult player2ScareResult = scareResultFactory.generateScareResult(player2);

        PlayerMatchupResult playerMatchupResult = new PlayerMatchupResult(player1, player2);

        playerMatchupResult.setPlayer1ScareResult(player1ScareResult);
        playerMatchupResult.setPlayer2ScareResult(player2ScareResult);

        applyComparisonLogic(playerMatchupResult, player1ScareResult, player2ScareResult);

        return playerMatchupResult;
    }

    private void applyComparisonLogic(PlayerMatchupResult result, ScareResult player1ScareResult, ScareResult player2ScareResult) {
        double player1ScareScore = player1ScareResult.getScareScore();
        double player2ScareScore = player2ScareResult.getScareScore();
        Player player1 = player1ScareResult.getPlayer();
        Player player2 = player2ScareResult.getPlayer();

        double difference = calculateScareScoreDifference(player1ScareScore, player2ScareScore);

        result.setScareDifference(difference);
        result.setAdvantage(determineAdvantage(difference));

        if (player1ScareScore > player2ScareScore) {
            setOutcome(result, player1, player2, player1ScareResult.getPrimaryExplanation());
        } else if (player2ScareScore > player1ScareScore) {
            setOutcome(result, player2, player1, player2ScareResult.getPrimaryExplanation());
        } else {
            result.setWinner(null);
            result.setLoser(null);
            result.setExplanation("Both players bring equal threat levels.");
        }

    }

    private void setOutcome(PlayerMatchupResult result, Player winner, Player loser, String reason) {
        result.setWinner(winner);
        result.setLoser(loser);
        result.setExplanation(winner.getName() + " wins the matchup because of: " + reason);
    }

    private double calculateScareScoreDifference(double scareFactor1, double scareFactor2) {
        return Math.abs(scareFactor1 - scareFactor2);
    }

    private MatchupAdvantages determineAdvantage(double difference) {
        if (difference <= 2) {
            return MatchupAdvantages.EVEN;
        } else if (difference <= 8) {
            return MatchupAdvantages.SLIGHT_EDGE;
        } else if (difference <= 15) {
            return MatchupAdvantages.CLEAR_EDGE;
        } else {
            return MatchupAdvantages.DOMINANT;
        }
    }
}
