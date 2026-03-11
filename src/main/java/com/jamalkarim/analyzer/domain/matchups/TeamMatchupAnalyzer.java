package com.jamalkarim.analyzer.domain.matchups;

import com.jamalkarim.analyzer.domain.entities.Player;
import com.jamalkarim.analyzer.domain.entities.Team;
import com.jamalkarim.analyzer.domain.enums.MatchupAdvantages;

import java.util.*;

/**
 * Service responsible for comparing two teams and predicting
 * which one has a statistical advantage.
 * <p>
 * The analysis involves aggregating Scare Factor scores, calculating
 * win probabilities, and identifying key star-player matchups.
 */
public class TeamMatchupAnalyzer {

    /**
     * Performs a comprehensive head-to-head analysis of two teams.
     * <p>
     * Both teams must have identical roster sizes. The analyzer
     * compares total impact scores and identifies the top 3 player
     * matchups by impact rank.
     *
     * @param team1 The first team to analyze.
     * @param team2 The second team to analyze.
     * @return A TeamMatchupResult containing scores, probabilities, and star matchups.
     * @throws RuntimeException if team sizes are not equal.
     */
    public TeamMatchupResult analyzeTeamMatchup(Team team1, Team team2) {

        if (!areTeamSizesEqual(team1, team2)) {
            throw new RuntimeException("Team roster sizes should be equal");
        }

        // initialize team names
        TeamMatchupResult teamMatchupResult = new TeamMatchupResult(team1.getName(), team2.getName());

        // sort maps so they are top-heavy
        Map<Player, Double> team1RosterSorted = setUpPlayerMaps(team1);
        Map<Player, Double> team2RosterSorted = setUpPlayerMaps(team2);

        double team1TotalScore = calculateTotalPointsForTeam(team1RosterSorted);
        double team2TotalScore = calculateTotalPointsForTeam(team2RosterSorted);

        // set total score for each team after summing up all scare factors
        teamMatchupResult.setTeam1TotalScore(team1TotalScore);
        teamMatchupResult.setTeam2TotalScore(team2TotalScore);

        // set team win probability based on total scores
        teamMatchupResult.setTeam1Probability(calculateWinProbability(team1TotalScore, team2TotalScore));
        teamMatchupResult.setTeam2Probability(calculateWinProbability(team2TotalScore, team1TotalScore));

        // determine advantage based on score difference
        teamMatchupResult.setAdvantage(determineAdvantage(Math.abs(team1TotalScore - team2TotalScore)));

        // generate top 3 player matchups
        List<Player> team1TopPlayers = getTop3PlayersFromTeam(team1RosterSorted);
        List<Player> team2TopPlayers = getTop3PlayersFromTeam(team2RosterSorted);

        List<PlayerMatchupResult> playerMatchupResults = generatePlayerMatchupResultsForTopPlayers(team1TopPlayers, team2TopPlayers);
        teamMatchupResult.setPlayerMatchupResults(playerMatchupResults);

        return teamMatchupResult;
    }

    private Map<Player, Double> setUpPlayerMaps(Team team) {

        if (team.getRoster().size() < 3) {
            throw new RuntimeException("Team roster must be greater than a size of 3");
        }

        List<Map.Entry<Player, Double>> entries = new ArrayList<>();

        for (Player player : team.getRoster()) {
            entries.add(Map.entry(player, player.calculateScareFactor()));
        }

        entries.sort(Map.Entry.<Player, Double>comparingByValue().reversed());

        Map<Player, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Player, Double> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    private List<Player> getTop3PlayersFromTeam(Map<Player, Double> roster) {
        List<Player> res = new LinkedList<>();
        int count = 0;
        for (Player player : roster.keySet()) {
            if (count >= 3) break;
            res.add(player);
            count++;
        }
        return res;
    }

    private List<PlayerMatchupResult> generatePlayerMatchupResultsForTopPlayers(List<Player> team1Players, List<Player> team2Players) {

        List<PlayerMatchupResult> listOfPlayerMatchups = new LinkedList<>();

        PlayerMatchupAnalyzer analyzer = new PlayerMatchupAnalyzer();

        for (int i = 0; i < team1Players.size(); i++) {
            PlayerMatchupResult result = analyzer.analyzePlayerMatchup(team1Players.get(i), team2Players.get(i));
            listOfPlayerMatchups.add(result);
        }

        return listOfPlayerMatchups;
    }

    private double calculateTotalPointsForTeam(Map<Player, Double> roster) {
        double total = 0;
        for (Double scareFactor : roster.values()) {
            total += scareFactor;
        }
        return total;
    }

    private double calculateWinProbability(double team1Score, double team2Score) {
        double difference = team1Score - team2Score;
        double probabilityForTeam = 1 / (1 + Math.exp(-difference / 20));
        return probabilityForTeam * 100;
    }

    private MatchupAdvantages determineAdvantage(double difference) {
        if (difference <= 10) {
            return MatchupAdvantages.EVEN;
        } else if (difference <= 30) {
            return MatchupAdvantages.SLIGHT_EDGE;
        } else if (difference <= 60) {
            return MatchupAdvantages.CLEAR_EDGE;
        } else {
            return MatchupAdvantages.DOMINANT;
        }
    }

    private boolean areTeamSizesEqual(Team team1, Team team2) {
        return team1.getRoster().size() == team2.getRoster().size();
    }
}
