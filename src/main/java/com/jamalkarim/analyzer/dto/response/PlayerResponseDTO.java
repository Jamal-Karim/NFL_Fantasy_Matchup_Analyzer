package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.dto.mock.MockStatsDTO;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "name", "team", "position"})
public class PlayerResponseDTO {

    private Long id;
    private String name;

    @JsonProperty("nfl_team")
    private String nflTeam;

    private Position position;

    @JsonProperty("is_rookie")
    private boolean rookie;

    @JsonProperty("is_injured")
    private boolean injured;

    @JsonProperty("current_season_stats")
    private MockStatsDTO currentSeasonStats;

    @JsonProperty("last_season_stats")
    private MockStatsDTO lastSeasonStats;
}