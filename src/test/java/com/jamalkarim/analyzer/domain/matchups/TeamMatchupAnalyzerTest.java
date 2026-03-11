package com.jamalkarim.analyzer.domain.matchups;

import com.jamalkarim.analyzer.domain.models.*;
import com.jamalkarim.analyzer.domain.enums.MatchupAdvantages;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.Stats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TeamMatchupAnalyzerTest {

    private TeamMatchupAnalyzer analyzer;
    private Team team1;
    private Team team2;

    @BeforeEach
    void setUp() {
        analyzer = new TeamMatchupAnalyzer();
        team1 = new Team("Eagles");
        team2 = new Team("Cowboys");
    }

    private Player createElitePlayer(Position position, String name, String teamName) {
        Player player;
        Stats stats = new Stats();
        stats.setGamesPlayed(17);

        switch (position) {
            case QB:
                player = new QuarterBack(name, teamName);
                stats.setPassingYards(4000);
                stats.setPassingTDs(35);
                stats.setInterceptions(5);
                break;
            case RB:
                player = new RunningBack(name, teamName);
                stats.setRushingYards(1500);
                stats.setRushingTDs(12);
                break;
            case WR:
                player = new WideReceiver(name, teamName);
                stats.setReceivingYards(1400);
                stats.setReceivingTDs(10);
                stats.setReceptions(100);
                break;
            case TE:
                player = new TightEnd(name, teamName);
                stats.setReceivingYards(800);
                stats.setReceivingTDs(8);
                stats.setReceptions(80);
                break;
            default:
                throw new IllegalArgumentException("Unknown position");
        }
        player.setLastSeasonStats(stats);
        return player;
    }

    private Player createAveragePlayer(Position position, String name, String teamName) {
        Player player;
        Stats stats = new Stats();
        stats.setGamesPlayed(17);

        switch (position) {
            case QB:
                player = new QuarterBack(name, teamName);
                stats.setPassingYards(3000);
                stats.setPassingTDs(15);
                stats.setInterceptions(15);
                break;
            case RB:
                player = new RunningBack(name, teamName);
                stats.setRushingYards(700);
                stats.setRushingTDs(4);
                break;
            case WR:
                player = new WideReceiver(name, teamName);
                stats.setReceivingYards(700);
                stats.setReceivingTDs(4);
                stats.setReceptions(50);
                break;
            case TE:
                player = new TightEnd(name, teamName);
                stats.setReceivingYards(300);
                stats.setReceivingTDs(2);
                stats.setReceptions(30);
                break;
            default:
                throw new IllegalArgumentException("Unknown position");
        }
        player.setLastSeasonStats(stats);
        return player;
    }

    @Test
    @DisplayName("Should correctly analyze a balanced team matchup")
    void shouldAnalyzeTeamMatchup() {
        // Build Team 1 (Elite)
        team1.addPlayer(createElitePlayer(Position.QB, "Jalen Hurts", "PHI"));
        team1.addPlayer(createElitePlayer(Position.RB, "Saquon Barkley", "PHI"));
        team1.addPlayer(createElitePlayer(Position.WR, "A.J. Brown", "PHI"));

        // Build Team 2 (Average)
        team2.addPlayer(createAveragePlayer(Position.QB, "Dak Prescott", "DAL"));
        team2.addPlayer(createAveragePlayer(Position.RB, "Ezekiel Elliott", "DAL"));
        team2.addPlayer(createAveragePlayer(Position.WR, "CeeDee Lamb", "DAL"));

        TeamMatchupResult result = analyzer.analyzeTeamMatchup(team1, team2);

        assertThat(result.getTeam1TotalScore())
                .as("Team 1 (Elite) should have a higher total score than Team 2")
                .isGreaterThan(result.getTeam2TotalScore());

        assertThat(result.getTeam1Probability())
                .as("Team 1 should have a win probability significantly over 50%")
                .isGreaterThan(60.0);

        assertThat(result.getPlayerMatchupResults())
                .as("Should contain 3 head-to-head battles")
                .hasSize(3);

        assertThat(result.getAdvantage())
                .as("Advantage should not be EVEN for an elite vs average matchup")
                .isNotEqualTo(MatchupAdvantages.EVEN);
    }

    @Test
    @DisplayName("Should throw exception if team sizes are unequal")
    void shouldThrowExceptionForUnequalTeamSizes() {
        team1.addPlayer(createElitePlayer(Position.QB, "QB1", "T1"));
        team1.addPlayer(createElitePlayer(Position.RB, "RB1", "T1"));

        team2.addPlayer(createElitePlayer(Position.QB, "QB2", "T2"));

        assertThatThrownBy(() -> analyzer.analyzeTeamMatchup(team1, team2))
                .as("Should fail because team sizes are not equal")
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("roster sizes should be equal");
    }

    @Test
    @DisplayName("Should throw exception if roster size is too small")
    void shouldThrowExceptionForSmallRoster() {
        team1.addPlayer(createElitePlayer(Position.QB, "QB1", "T1"));
        team1.addPlayer(createElitePlayer(Position.RB, "RB1", "T1"));

        team2.addPlayer(createElitePlayer(Position.QB, "QB2", "T2"));
        team2.addPlayer(createElitePlayer(Position.RB, "RB2", "T2"));

        assertThatThrownBy(() -> analyzer.analyzeTeamMatchup(team1, team2))
                .as("Should fail because roster size is less than 3")
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("must be greater than a size of 3");
    }
}
