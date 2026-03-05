package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;

public class Runningback extends Player{
    private int rushingAttempts;
    private int rushingYards;
    private int receptions;
    private int receivingYards;
    private int rushingTouchdowns;
    private int receivingTouchdowns;

    public Runningback(String name, String team, int rushingAttempts, int rushingYards,
                       int receptions, int receivingYards, int rushingTouchdowns, int receivingTouchdowns) {
        super(name, team, Position.RB);
        this.rushingAttempts = rushingAttempts;
        this.rushingYards = rushingYards;
        this.receptions = receptions;
        this.receivingYards = receivingYards;
        this.rushingTouchdowns = rushingTouchdowns;
        this.receivingTouchdowns = receivingTouchdowns;
    }

    @Override
    public double calculateScareFactor() {
        return 0;
    }
}
