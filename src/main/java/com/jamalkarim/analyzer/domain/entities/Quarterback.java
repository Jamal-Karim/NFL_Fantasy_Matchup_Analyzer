package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;

public class Quarterback extends Player{

    public Quarterback(String name, String team) {
        super(name, team, Position.QB);
    }

    @Override
    public double calculateScareFactor() {
        return 0;
    }
}
