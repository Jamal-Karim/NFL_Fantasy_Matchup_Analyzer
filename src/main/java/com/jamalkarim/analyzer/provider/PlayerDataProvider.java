package com.jamalkarim.analyzer.provider;

import com.jamalkarim.analyzer.domain.models.Player;

/**
 * Interface for fetching player data from external sources.
 */
public interface PlayerDataProvider {

    /**
     * Fetches a player's statistical data.
     *
     * @param name The name of the player
     * @param nflTeam The NFL team the player plays for
     * @return A Player domain object populated with fetched data, or null if not found
     */
    Player fetchPlayer(String name, String nflTeam);
}
