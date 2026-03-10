package com.jamalkarim.analyzer.domain.scoring;

import com.jamalkarim.analyzer.domain.entities.Player;
import com.jamalkarim.analyzer.domain.enums.PlayerTier;

import java.util.List;

public class ScareResultFactory {

    public ScareResult generateScareResult(Player player){
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
