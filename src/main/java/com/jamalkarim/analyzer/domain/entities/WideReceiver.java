package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;

public class WideReceiver extends Player{

    public WideReceiver(String name, String team) {
        super(name, team, Position.WR);
    }

    @Override
    public double calculateScareFactor() {
        return 0;
    }

}
