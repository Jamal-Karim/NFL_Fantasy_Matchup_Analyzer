package com.jamalkarim.analyzer.domain.stats;

import java.util.List;

public interface ScareFactor {

    double calculateScareFactor();

    List<String> generateListOfExplanations();
}
