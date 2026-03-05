package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;

public class WideReceiver extends Player{
    private int receptions;
    private int receivingYards;
    private int receivingTouchdowns;
    private int rushingYards;
    private int rushingTouchdowns;

    public WideReceiver(String name, String team, int rushingYards,
                       int receptions, int receivingYards, int rushingTouchdowns, int receivingTouchdowns) {
        super(name, team, Position.WR);
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
