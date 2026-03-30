package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.scoring.ScareResult;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.dto.response.SimulationResponseDTO;
import com.jamalkarim.analyzer.utils.NumberUtils;
import com.jamalkarim.analyzer.utils.ScareResultMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Random;

@Service
public class SimulationService {

    private final ScareResultMapper scareResultMapper;
    private final ScareResultService scareResultService;

    public SimulationService(ScareResultMapper scareResultMapper, ScareResultService scareResultService) {
        this.scareResultMapper = scareResultMapper;
        this.scareResultService = scareResultService;
    }

    public SimulationResponseDTO runSimulation(long id) {

        ScareResponseDTO scareResult = scareResultService.getScareResultById(id);

        SimulationResponseDTO responseDTO = scareResultMapper.responseToSimulationResponse(scareResult);

        double mean = scareResult.getScareScore();
        double baseVolatility = scareResult.getPosition().getBaseVolatility();

        double[] results = new double[10000];
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            double gaussian = random.nextGaussian();

            double simulatedScore = mean + (gaussian * baseVolatility * 30);

            results[i] = Math.max(0, Math.min(100, simulatedScore));
        }

        analyzeResults(results, responseDTO);

        return responseDTO;
    }

    private void analyzeResults(double[] results, SimulationResponseDTO result) {
        Arrays.sort(results);
        double median = results[5000];
        result.setMeanScareScore(NumberUtils.round(median));

        result.setFloorScore(NumberUtils.round(results[1000]));

        result.setCeilingScore(NumberUtils.round(results[9500]));

        double bustThreshold = median * 0.75;
        long bustCount = Arrays.stream(results).filter(s -> s < bustThreshold).count();
        result.setBustPercentage(NumberUtils.round((bustCount / 10000.0) * 100));

        double boomThreshold;
        if (median > 85) {
            boomThreshold = 97.0;
        } else {
            boomThreshold = median * 1.15;
        }

        long boomCount = Arrays.stream(results).filter(s -> s > boomThreshold).count();
        result.setBoomPercentage(NumberUtils.round((boomCount / 10000.0) * 100));
    }
}
