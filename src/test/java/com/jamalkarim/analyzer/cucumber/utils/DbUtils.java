package com.jamalkarim.analyzer.cucumber.utils;

import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.PlayerMatchupResultEntity;
import com.jamalkarim.analyzer.entities.TeamEntity;
import com.jamalkarim.analyzer.entities.TeamMatchupResultEntity;
import com.jamalkarim.analyzer.repository.PlayerMatchupRepository;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.repository.TeamMatchupRepository;
import com.jamalkarim.analyzer.repository.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Utility class for performing direct database verifications and state management.
 * Breaks the 'fourth wall' to ensure data integrity beyond API responses.
 */
@Component
public class DbUtils {

    private final JdbcTemplate jdbcTemplate;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerMatchupRepository playerMatchupRepository;
    private final TeamMatchupRepository teamMatchupRepository;

    public DbUtils(JdbcTemplate jdbcTemplate, PlayerRepository playerRepository, TeamRepository teamRepository, PlayerMatchupRepository playerMatchupRepository, TeamMatchupRepository teamMatchupRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.playerMatchupRepository = playerMatchupRepository;
        this.teamMatchupRepository = teamMatchupRepository;
    }

    /**
     * Verifies that a player with the given name exists in the database.
     */
    public void verifyPlayerIsSaved(String name) {
        Optional<PlayerEntity> player = playerRepository.findByName(name);
        assertThat(player)
                .withFailMessage("Expected player %s to be in the database, but was not.", name)
                .isPresent();
    }

    /**
     * Verifies that a team with the given name exists in the database.
     */
    public void verifyTeamIsSaved(String name) {
        Optional<TeamEntity> team = teamRepository.findByName(name);
        assertThat(team)
                .withFailMessage("Expected team %s to be in the database, but was not.", name)
                .isPresent();
    }

    /**
     * Verifies that a team with the given name does NOT exist in the database.
     */
    public void verifyTeamDoesNotExist(String name) {
        Optional<TeamEntity> team = teamRepository.findByName(name);
        assertThat(team)
                .withFailMessage("Expected team %s to be deleted from the database, but it still exists.", name)
                .isNotPresent();
    }

    /**
     * Verifies that statistical entities for a player are properly persisted and linked.
     */
    public void verifyPlayerStatsAreSaved(String playerName) {
        String sql = "SELECT p.name, current_stats_id, last_stats_id " +
                "FROM player p WHERE p.name = ?";
        
        Map<String, Object> results = jdbcTemplate.queryForMap(sql, playerName);
        
        assertThat(results.get("current_stats_id"))
                .withFailMessage("Current season stats for player %s were not saved.", playerName)
                .isNotNull();
        
        assertThat(results.get("last_stats_id"))
                .withFailMessage("Last season stats for player %s were not saved.", playerName)
                .isNotNull();
    }

    /**
     * Verifies that a Scare Factor result is saved for a specific player ID.
     */
    public void verifyScareResultIsSaved(Long id) {
        String sql = "SELECT count(*) AS count FROM scare_result WHERE player_id = ?";
        Map<String, Object> results = jdbcTemplate.queryForMap(sql, id);
        Long count = (Long) results.get("count");
        Assertions.assertEquals(1, count);
    }

    /**
     * Verifies if a player is correctly associated with a team in the database.
     *
     * @param playerName       The name of the player
     * @param expectedTeamName The expected name of the team
     * @param present          True if the player should be on the team, false if they should not
     */
    public void verifyPlayerIsOnTeam(String playerName, String expectedTeamName, boolean present) {
        String sql = "SELECT p.name AS player_name, t.name AS team_name " +
                "FROM player p LEFT JOIN team t ON p.team_id = t.id " +
                "WHERE p.name = ?";

        try {
            Map<String, Object> results = jdbcTemplate.queryForMap(sql, playerName);
            Object actualTeamObj = results.get("team_name");

            if (present) {
                assertThat(actualTeamObj)
                        .withFailMessage("Expected player [%s] to be on team [%s], but they are not assigned to any team.", playerName, expectedTeamName)
                        .isNotNull();

                Assertions.assertEquals(expectedTeamName, actualTeamObj.toString());
            } else {
                assertThat(actualTeamObj)
                        .withFailMessage("Expected player [%s] to NOT be on a team, but they are assigned to [%s].", playerName, actualTeamObj)
                        .isNull();
            }
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assertions.fail("Database Error: Player [" + playerName + "] does not exist in the database.");
        }
    }

    /**
     * Verifies that a head-to-head player matchup result is correctly stored.
     */
    public void verifyPlayerMatchupIsSaved(long id, String player1Name, String player2Name) {
        Optional<PlayerMatchupResultEntity> playerMatchupResult = playerMatchupRepository.findById(id);
        assertThat(playerMatchupResult)
                .withFailMessage("Expected matchup %s to be in the database, but was not.", playerMatchupResult)
                .isPresent();

        String sql = "SELECT p1.name AS p1_name, p2.name AS p2_name " +
                "FROM player_matchup m " +
                "JOIN player p1 ON m.player_1_id = p1.id " +
                "JOIN player p2 ON m.player_2_id = p2.id " +
                "WHERE m.id = ?";

        try {
            Map<String, Object> results = jdbcTemplate.queryForMap(sql, id);

            Assertions.assertEquals(player1Name, results.get("p1_name").toString(), "Player 1 mismatch!");
            Assertions.assertEquals(player2Name, results.get("p2_name").toString(), "Player 2 mismatch!");

        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            Assertions.fail("Database Error: Matchup ID [" + id + "] not found in player_matchup table.");
        }
    }

    /**
     * Verifies the count of player battle records associated with a team matchup.
     */
    public void verifyTeamMatchupIsSaved(long id, int expectedAmount) {
        Optional<TeamMatchupResultEntity> teamMatchupResult = teamMatchupRepository.findById(id);
        assertThat(teamMatchupResult)
                .withFailMessage("Expected matchup %s to be in the database, but was not.", teamMatchupResult)
                .isPresent();

        String sql = "SELECT count(*) AS count FROM player_matchup WHERE team_matchup_id = ?";

        Integer actualCount = jdbcTemplate.queryForObject(sql, Integer.class, id);

        Assertions.assertEquals(expectedAmount, actualCount, "Player matchup count mismatch");
    }

    /**
     * Truncates all tables to ensure a clean state before each scenario.
     * Uses FOREIGN_KEY_CHECKS=0 to handle circular dependencies.
     */
    public void clearDatabase() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        jdbcTemplate.execute("TRUNCATE TABLE player_matchup");
        jdbcTemplate.execute("TRUNCATE TABLE team_matchup");
        jdbcTemplate.execute("TRUNCATE TABLE scare_result");
        jdbcTemplate.execute("TRUNCATE TABLE player");
        jdbcTemplate.execute("TRUNCATE TABLE stats");
        jdbcTemplate.execute("TRUNCATE TABLE team");

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }
}
