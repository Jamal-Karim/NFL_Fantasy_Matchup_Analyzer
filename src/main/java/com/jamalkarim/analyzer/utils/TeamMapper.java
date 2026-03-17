package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.TeamEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper utility for converting Team-related objects between different layers.
 * Handles conversions between JPA Entities and Domain Models.
 */
@Component
public class TeamMapper {

    private final PlayerMapper playerMapper;

    public TeamMapper(PlayerMapper playerMapper) {
        this.playerMapper = playerMapper;
    }

    /**
     * Converts a TeamEntity to its corresponding domain model.
     * Maps all players in the roster using PlayerMapper.
     *
     * @param entity The database entity
     * @return A Team domain model
     */
    public Team entityToDomain(TeamEntity entity) {
        Team team = new Team(entity.getName());

        List<Player> roster = new ArrayList<>();

        for (PlayerEntity playerEntity : entity.getRoster()) {
            roster.add(playerMapper.entityToDomain(playerEntity));
        }

        team.setId(entity.getId());
        team.setRoster(roster);
        return team;
    }
}
