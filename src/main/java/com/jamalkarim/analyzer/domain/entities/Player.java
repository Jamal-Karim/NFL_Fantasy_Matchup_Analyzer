package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.domain.interfaces.ScareFactor;

public abstract class Player implements ScareFactor {

    private String name;
    private String team;
    private Position position;

    public Player(String name, String team, Position position) {
        this.name = name;
        this.team = team;
        this.position = position;
    }
}
