package com.jamalkarim.analyzer.domain.matchups;

import com.jamalkarim.analyzer.domain.entities.Player;
import com.jamalkarim.analyzer.domain.enums.MatchupAdvantages;
import lombok.Data;

import java.util.List;

@Data
public class TeamMatchupResult {
    private String team1;
    private String team2;
    private double team1TotalScore;
    private double team2TotalScore;
    private double team1Probability;
    private double team2Probability;
    private MatchupAdvantages advantage;
    private List<PlayerMatchupResult> playerMatchupResults;

    public TeamMatchupResult(String team1, String team2) {
        this.team1 = team1;
        this.team2 = team2;
    }

    public void printTeamMatchupReport() {
        String separator = "==================================================";
        String subSeparator = "--------------------------------------------------";

        System.out.println("\n" + separator);
        System.out.println("            TEAM MATCHUP: " + team1.toUpperCase() + " vs " + team2.toUpperCase());
        System.out.println(separator);

        System.out.println("TOTAL SCARE SCORES:");
        System.out.printf("%s: %.0f points\n", team1, team1TotalScore);
        System.out.printf("%s: %.0f points\n", team2, team2TotalScore);

        System.out.println(subSeparator);

        System.out.println("WIN PROBABILITY:");
        System.out.printf("%s: %.1f%% | %s: %.1f%%\n", team1, team1Probability, team2, team2Probability);
        System.out.println("OVERALL ADVANTAGE: " + advantage);

        System.out.println(subSeparator);
        System.out.println("KEY HEAD-TO-HEAD BATTLES:");

        if (playerMatchupResults != null && !playerMatchupResults.isEmpty()) {
            int rank = 1;
            for (PlayerMatchupResult battle : playerMatchupResults) {
                String winnerDisplay = battle.getWinner()
                        .map(Player::getName)      // Get just the name if present
                        .orElse("Draw");

                System.out.printf("%d. %-15s vs  %-15s -> %s\n",
                        rank,
                        battle.getPlayer1().getName(),
                        battle.getPlayer2().getName(),
                        winnerDisplay);
                rank++;
            }
        }

        System.out.println(separator + "\n");
    }
}
