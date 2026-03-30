package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import com.jamalkarim.analyzer.domain.enums.Position;
import lombok.Data;

@Data
@JsonPropertyOrder({
        "name",
        "nfl_team",
        "position",
        "scare_tier",
        "avg_scare_score",
        "best_scare_score",
        "worst_scare_score",
        "boom_percentage",
        "bust_percentage"
})
public class SimulationResponseDTO {

    private String name;

    @JsonProperty("nfl_team")
    private String team;

    private Position position;

    @JsonProperty("scare_tier")
    private PlayerTier scareTier;

    @JsonProperty("avg_scare_score")
    private double meanScareScore;

    @JsonProperty("worst_scare_score")
    private double floorScore;

    @JsonProperty("best_scare_score")
    private double ceilingScore;

    @JsonProperty("boom_percentage")
    private double boomPercentage;

    @JsonProperty("bust_percentage")
    private double bustPercentage;
}
