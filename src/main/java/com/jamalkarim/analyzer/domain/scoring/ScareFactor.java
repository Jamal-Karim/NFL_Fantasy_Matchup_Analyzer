package com.jamalkarim.analyzer.domain.scoring;

import java.util.List;

/**
 * Defines the core contract for entities that can be analyzed for their "Scare Factor" 
 * in a fantasy football context.
 * 
 * The Scare Factor is a metric (0.0 - 99.9) that represents the
 * threat level a player poses to their opponent.
 */
public interface ScareFactor {

    /**
     * Calculates the final impact score for a player.
     * 
     * @return A double between 0.0 and 99.9 representing the player's threat level.
     */
    double calculateScareFactor();

    /**
     * Generates a prioritized list of strings explaining the statistical reasons
     * behind the player's current Scare Factor.
     * 
     * @return A list of descriptive strings, with the most impactful reason first.
     */
    List<String> generateListOfExplanations();
}
