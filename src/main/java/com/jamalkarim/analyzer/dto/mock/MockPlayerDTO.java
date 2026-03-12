package com.jamalkarim.analyzer.dto.mock;

import com.jamalkarim.analyzer.domain.enums.Position;
import lombok.Data;

@Data
public class MockPlayerDTO {
    private String name;
    private String nflTeam;
    private Position position;
    private int draftPick;
    private boolean isRookie;
    private boolean isInjured;
    private MockStatsDTO currentSeasonStats;
    private MockStatsDTO lastSeasonStats;
}
