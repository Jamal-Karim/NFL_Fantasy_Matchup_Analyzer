package com.jamalkarim.analyzer.cucumber.utils;

import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.TeamEntity;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import com.jamalkarim.analyzer.repository.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Component
public class DbUtils {

    private final JdbcTemplate jdbcTemplate;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public DbUtils(JdbcTemplate jdbcTemplate, PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    public void verifyPlayerIsSaved(String name) {
        Optional<PlayerEntity> player = playerRepository.findByName(name);
        assertThat(player)
                .withFailMessage("Expected player %s to be in the database, but was not.", name)
                .isPresent();
    }

    public void verifyTeamIsSaved(String name) {
        Optional<TeamEntity> team = teamRepository.findByName(name);
        assertThat(team)
                .withFailMessage("Expected team %s to be in the database, but was not.", name)
                .isPresent();
    }

    public void verifyScareResultIsSaved(Long id) {
        String sql = "SELECT count(*) AS count FROM scare_result WHERE player_id = ?";
        Map<String, Object> results = jdbcTemplate.queryForMap(sql, id);
        Long count = (Long) results.get("count");
        Assertions.assertEquals(1, count);
    }

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

    public void clearDatabase() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        jdbcTemplate.execute("TRUNCATE TABLE scare_result");
        jdbcTemplate.execute("TRUNCATE TABLE player");
        jdbcTemplate.execute("TRUNCATE TABLE stats");
        jdbcTemplate.execute("TRUNCATE TABLE team");

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }
}
