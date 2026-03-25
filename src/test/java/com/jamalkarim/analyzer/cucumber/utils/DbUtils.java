package com.jamalkarim.analyzer.cucumber.utils;

import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.repository.PlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Component
public class DbUtils {

    private final JdbcTemplate jdbcTemplate;
    private final PlayerRepository playerRepository;

    public DbUtils(JdbcTemplate jdbcTemplate, PlayerRepository playerRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.playerRepository = playerRepository;
    }

    public void verifyPlayerIsSaved(String name) {
        Optional<PlayerEntity> player = playerRepository.findByName(name);
        assertThat(player)
                .withFailMessage("Expected player %s to be in the database, but was not.", name)
                .isPresent();
    }

    public void verifyScareResultIsSaved(Long id) {
        String sql = "SELECT count(*) AS count FROM scare_result WHERE player_id = ?";
        Map<String, Object> results = jdbcTemplate.queryForMap(sql, id);
        Long count = (Long) results.get("count");
        Assertions.assertEquals(1, count);
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
