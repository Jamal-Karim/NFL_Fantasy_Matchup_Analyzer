package com.jamalkarim.analyzer.utils;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.entities.PlayerEntity;
import com.jamalkarim.analyzer.entities.TeamEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TeamMapper {

    private final PlayerMapper playerMapper;

    public TeamMapper(PlayerMapper playerMapper) {
        this.playerMapper = playerMapper;
    }

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

    public TeamEntity domainToEntity(Team domain) {
        TeamEntity entity = new TeamEntity();
        entity.setName(domain.getName());
        return entity;
    }
}
