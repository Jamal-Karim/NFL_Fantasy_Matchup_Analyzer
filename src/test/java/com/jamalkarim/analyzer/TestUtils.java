package com.jamalkarim.analyzer;

import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.ScareResultEntity;
import com.jamalkarim.analyzer.entities.StatsEntity;

public class TestUtils {

    public static PlayerEntity createTestPlayer(String name, String team, Position position) {
        PlayerEntity player = new PlayerEntity();
        player.setName(name);
        player.setNflTeam(team);
        player.setPosition(position);

        StatsEntity stats = new StatsEntity();
        stats.setSeason(2023);
        player.setCurrentSeasonStats(stats);

        ScareResultEntity scare = new ScareResultEntity();
        scare.setScareScore(50.0);
        scare.setPlayerTier(PlayerTier.AVERAGE);
        scare.setPlayer(player);
        player.setScareResult(scare);

        return player;
    }
}
