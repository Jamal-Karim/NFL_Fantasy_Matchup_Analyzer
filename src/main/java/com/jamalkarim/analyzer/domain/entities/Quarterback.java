package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;

public class Quarterback extends Player{
    private int passingYards;
    private int rushingYards;
    private int passingTouchdowns;
    private int rushingTouchdowns;
    private int interceptions;

    public Quarterback(String name, String team, int passingYards, int rushingYards,
                       int passingTouchdowns, int rushingTouchdowns, int interceptions) {
        super(name, team, Position.QB);
        this.passingYards = passingYards;
        this.rushingYards = rushingYards;
        this.passingTouchdowns = passingTouchdowns;
        this.rushingTouchdowns = rushingTouchdowns;
        this.interceptions = interceptions;
    }

    @Override
    public double calculateScareFactor() {
        return 0;
    }
}
