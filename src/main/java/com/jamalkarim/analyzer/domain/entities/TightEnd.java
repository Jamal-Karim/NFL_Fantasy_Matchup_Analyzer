package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;

public class TightEnd extends Player{
    private int receptions;
    private int receivingYards;
    private int receivingTouchdowns;

    public TightEnd(String name, String team, int receptions, int receivingYards, int receivingTouchdowns) {
        super(name, team, Position.TE);
        this.receptions = receptions;
        this.receivingYards = receivingYards;
        this.receivingTouchdowns = receivingTouchdowns;
    }

    @Override
    public double calculateScareFactor() {
        return 0;
    }

}
