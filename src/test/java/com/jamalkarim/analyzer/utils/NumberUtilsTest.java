package com.jamalkarim.analyzer.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NumberUtilsTest {

    @Test
    void testRound() {
        assertEquals(1.23, NumberUtils.round(1.2345));
        assertEquals(1.24, NumberUtils.round(1.2356));
        assertEquals(1.00, NumberUtils.round(1.0));
        assertEquals(1.20, NumberUtils.round(1.2));
    }
}
