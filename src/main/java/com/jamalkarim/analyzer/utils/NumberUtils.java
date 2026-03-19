package com.jamalkarim.analyzer.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {

    /**
     * Rounds a double to 2 decimal places.
     * Use this for scores, scare differences, etc.
     */
    public static double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}