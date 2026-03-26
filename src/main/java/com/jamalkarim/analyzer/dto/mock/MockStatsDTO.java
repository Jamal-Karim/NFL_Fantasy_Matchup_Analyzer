package com.jamalkarim.analyzer.dto.mock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer Object for mapping statistical data from mock JSON files.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MockStatsDTO {

    @Schema(example = "2025")
    private Integer season;

    @Schema(example = "17")
    @JsonProperty("games_played")
    private Integer gamesPlayed;

    @Schema(example = "460")
    @JsonProperty("passing_attempts")
    private Integer passAttempts;

    @Schema(example = "319")
    private Integer completions;

    @Schema(example = "3668")
    @JsonProperty("passing_yards")
    private Integer passingYards;

    @Schema(example = "25")
    @JsonProperty("passing_tds")
    private Integer passingTDs;

    @Schema(example = "10")
    private Integer interceptions;

    @Schema(example = "112")
    @JsonProperty("rushing_attempts")
    private Integer rushingAttempts;

    @Schema(example = "579")
    @JsonProperty("rushing_yards")
    private Integer rushingYards;

    @Schema(example = "14")
    @JsonProperty("rushing_tds")
    private Integer rushingTDs;

    @Schema(example = "110")
    private Integer receptions;

    @Schema(example = "1000")
    @JsonProperty("receiving_yards")
    private Integer receivingYards;

    @Schema(example = "8")
    @JsonProperty("receiving_tds")
    private Integer receivingTDs;
}
