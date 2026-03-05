package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.stats.ScareFactor;
import com.jamalkarim.analyzer.domain.stats.Stats;
import lombok.Data;

@Data
public abstract class Player implements ScareFactor {

    private String name;
    private String team;
    private Position position;
    private Stats currentSeasonStats;
    private Stats lassSeasonStats;

    public Player(String name, String team, Position position) {
        this.name = name;
        this.team = team;
        this.position = position;
    }
}
