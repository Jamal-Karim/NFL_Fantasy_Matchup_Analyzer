package com.jamalkarim.analyzer.dto.mock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jamalkarim.analyzer.domain.enums.Position;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MockPlayerDTO {

    private String name;

    @JsonProperty("nfl_team")
    private String nflTeam;

    private Position position;

    @JsonProperty("draft_pick")
    private int draftPick;

    @JsonProperty("is_rookie")
    private boolean isRookie;

    @JsonProperty("is_injured")
    private boolean isInjured;

    @JsonProperty("current_season_stats")
    private MockStatsDTO currentSeasonStats;

    @JsonProperty("last_season_stats")
    private MockStatsDTO lastSeasonStats;
}
