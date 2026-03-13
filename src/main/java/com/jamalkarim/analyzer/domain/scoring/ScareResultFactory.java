package com.jamalkarim.analyzer.domain.scoring;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Factory class responsible for orchestrating the analysis of a Player entity
 * and packaging the results into a ScareResult report.
 */
@Component
public class ScareResultFactory {

    /**
     * Performs a full "Scare Factor" analysis on a player.
     *
     * @param player The player entity to analyze.
     * @return A ScareResult containing the final score, tier, and textual explanations.
     */
    public ScareResult generateScareResult(Player player) {
        ScareResult scareResult = new ScareResult(player);

        double scareScore = player.calculateScareFactor();
        List<String> allExplanations = player.generateListOfExplanations();

        scareResult.setScareScore(scareScore);
        scareResult.setScareTier(PlayerTier.fromScore(scareScore));

        scareResult.setPrimaryExplanation(player.findPrimaryExplanation(allExplanations));
        scareResult.setSupportingExplanations(player.findSupportingExplanations(allExplanations));

        return scareResult;
    }
}
