package com.jamalkarim.analyzer.provider;

import com.jamalkarim.analyzer.domain.models.Player;
import com.jamalkarim.analyzer.dto.mock.MockPlayerDTO;

public interface PlayerDataProvider {

    MockPlayerDTO fetchPlayer(String name, String nflTeam);
}
