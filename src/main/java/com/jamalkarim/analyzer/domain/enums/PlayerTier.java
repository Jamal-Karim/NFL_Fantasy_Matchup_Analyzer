package com.jamalkarim.analyzer.domain.enums;

public enum PlayerTier {
    ELITE(90, 101),
    SCARY(80, 89.99),
    STRONG(70, 79.99),
    AVERAGE(50, 69.99),
    WEAK(0, 49.99);

    private final double min;
    private final double max;

    PlayerTier(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public static PlayerTier fromScore(double score) {
        for (PlayerTier tier : PlayerTier.values()) {
            if (score >= tier.min && score <= tier.max) {
                return tier;
            }
        }
        return WEAK;
    }
}
