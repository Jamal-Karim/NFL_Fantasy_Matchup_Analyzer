package com.jamalkarim.analyzer.domain.enums;

import lombok.Getter;

/**
 * Enum to contain the positions on an NFL fantasy team
 */
@Getter
public enum Position {
    QB(0.15),
    RB(0.25),
    WR(0.35),
    TE(0.45);

    private final double baseVolatility;

    Position(double baseVolatility) {
        this.baseVolatility = baseVolatility;
    }
}
