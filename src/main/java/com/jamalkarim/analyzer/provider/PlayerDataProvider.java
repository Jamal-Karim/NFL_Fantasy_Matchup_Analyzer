package com.jamalkarim.analyzer.provider;

import com.jamalkarim.analyzer.domain.models.Player;

public interface PlayerDataProvider {

    Player fetchPlayer(String name, String nflTeam);
}
