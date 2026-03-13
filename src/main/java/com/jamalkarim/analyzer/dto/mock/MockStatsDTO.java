package com.jamalkarim.analyzer.dto.mock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MockStatsDTO {

    private Integer season;

    @JsonProperty("games_played")
    private Integer gamesPlayed;

    @JsonProperty("passing_attempts")
    private Integer passAttempts;

    private Integer completions;

    @JsonProperty("passing_yards")
    private Integer passingYards;

    @JsonProperty("passing_tds")
    private Integer passingTDs;

    private Integer interceptions;

    @JsonProperty("rushing_attempts")
    private Integer rushingAttempts;

    @JsonProperty("rushing_yards")
    private Integer rushingYards;

    @JsonProperty("rushing_tds")
    private Integer rushingTDs;

    private Integer receptions;

    @JsonProperty("receiving_yards")
    private Integer receivingYards;

    @JsonProperty("receiving_tds")
    private Integer receivingTDs;
}
