package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;

public class Runningback extends Player{

    public Runningback(String name, String team) {
        super(name, team, Position.RB);
    }

    @Override
    public double calculateScareFactor() {
        return 0;
    }
}
