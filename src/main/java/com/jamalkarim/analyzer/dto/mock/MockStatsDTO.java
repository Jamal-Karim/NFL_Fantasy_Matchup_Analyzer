package com.jamalkarim.analyzer.dto.mock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MockStatsDTO {

    private int season;

    @JsonProperty("games_played")
    private int gamesPlayed;

    @JsonProperty("passing_attempts")
    private int passAttempts;

    private int completions;

    @JsonProperty("passing_yards")
    private int passingYards;

    @JsonProperty("passing_tds")
    private int passingTDs;

    private int interceptions;

    @JsonProperty("rushing_attempts")
    private int rushingAttempts;

    @JsonProperty("rushing_yards")
    private int rushingYards;

    @JsonProperty("rushing_tds")
    private int rushingTDs;

    private int receptions;

    @JsonProperty("receiving_yards")
    private int receivingYards;

    @JsonProperty("receiving_tds")
    private int receivingTDs;
}
