package com.jamalkarim.analyzer.domain.entities;

import com.jamalkarim.analyzer.domain.enums.Position;

public class TightEnd extends Player{

    public TightEnd(String name, String team) {
        super(name, team, Position.TE);
    }

    @Override
    public double calculateScareFactor() {
        return 0;
    }

}
