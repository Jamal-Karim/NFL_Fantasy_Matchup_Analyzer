package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jamalkarim.analyzer.domain.enums.Position;
import com.jamalkarim.analyzer.dto.mock.MockStatsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer Object for detailed player information.
 * Used for delivering player data through the API.
 */
@Data
@JsonPropertyOrder({"id", "name", "team", "position"})
@Schema(description = "Comprehensive details for an NFL player, including biographical and statistical data")
public class PlayerResponseDTO {

    @Schema(description = "Internal database ID of the player", example = "101")
    private Long id;

    @Schema(description = "Full name of the player", example = "Josh Allen")
    private String name;

    @JsonProperty("nfl_team")
    @Schema(description = "Abbreviation of the NFL team", example = "BUF")
    private String nflTeam;

    @Schema(description = "Standard NFL position code", example = "QB")
    private Position position;

    @JsonProperty("is_rookie")
    @Schema(description = "Indicates if the player is in their first NFL season", example = "false")
    private boolean rookie;

    @JsonProperty("is_injured")
    @Schema(description = "Indicates if the player currently has an injury status", example = "false")
    private boolean injured;

    @JsonProperty("current_season_stats")
    @Schema(description = "Aggregated statistics for the ongoing NFL season")
    private MockStatsDTO currentSeasonStats;

    @JsonProperty("last_season_stats")
    @Schema(description = "Aggregated statistics for the previous completed NFL season")
    private MockStatsDTO lastSeasonStats;
}