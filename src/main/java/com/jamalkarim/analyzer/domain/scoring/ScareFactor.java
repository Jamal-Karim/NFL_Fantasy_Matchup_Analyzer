package com.jamalkarim.analyzer.domain.scoring;

import java.util.List;

public interface ScareFactor {

    double calculateScareFactor();

    List<String> generateListOfExplanations();
}
