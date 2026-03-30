package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.scoring.ScareResult;

import java.util.Arrays;
import java.util.Random;

public class SimulationService {

    public void runSimulation(ScareResult scareResult) {
        double mean = scareResult.getScareScore();
        double baseVolatility = scareResult.getPosition().getBaseVolatility();

        double[] results = new double[10000];
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            double gaussian = random.nextGaussian();

            double simulatedScore = mean + (gaussian * baseVolatility * 10);

            results[i] = Math.max(0, Math.min(100, simulatedScore));
        }

        analyzeResults(results);
    }

    private void analyzeResults(double[] results) {
        Arrays.sort(results);

        double floor = results[1000];
        double median = results[5000];
        double ceiling = results[9000];

        double bustRating = 40.0;
        long badGames = Arrays.stream(results).filter(score -> score < bustRating).count();

        double badGameProbability = (badGames / 10000.0) * 100;
    }
}
