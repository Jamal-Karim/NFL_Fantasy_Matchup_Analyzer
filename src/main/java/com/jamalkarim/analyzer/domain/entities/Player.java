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
    private int draftPick;
    private boolean isRookie;
    private boolean isInjured;
    private Stats currentSeasonStats;
    private Stats lastSeasonStats;

    public Player(String name, String team, Position position) {
        this.name = name;
        this.team = team;
        this.position = position;
    }
}
