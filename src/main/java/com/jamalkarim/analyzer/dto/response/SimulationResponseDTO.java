package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import com.jamalkarim.analyzer.domain.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer Object representing the results of a Monte Carlo simulation.
 * Contains probabilistic projections including floor/ceiling scores and boom/bust percentages.
 */
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
@Schema(description = "Analytical output containing the results of a Monte Carlo simulation")
public class SimulationResponseDTO {

    @Schema(description = "Full name of the analyzed player", example = "Josh Allen")
    private String name;

    @JsonProperty("nfl_team")
    @Schema(description = "NFL team abbreviation", example = "BUF")
    private String team;

    @Schema(description = "Player position", example = "QB")
    private Position position;

    @JsonProperty("scare_tier")
    @Schema(description = "Categorical performance tier based on the Scare Score", example = "ELITE")
    private PlayerTier scareTier;

    @JsonProperty("avg_scare_score")
    @Schema(description = "Numerical performance risk score (0-100), where higher indicates better projected performance", example = "85.5")
    private double meanScareScore;

    @Schema(description = "The worst score a player could potentially get")
    @JsonProperty("worst_scare_score")
    private double floorScore;

    @Schema(description = "The best score a player could potentially get")
    @JsonProperty("best_scare_score")
    private double ceilingScore;

    /**
     * The percentage of simulations where the player significantly exceeded their mean
     */
    @JsonProperty("boom_percentage")
    @Schema(description = "The percentage for how likely a player is to perform above their average")
    private double boomPercentage;

    /**
     * The percentage of simulations where the player fell significantly below their mean
     */
    @JsonProperty("bust_percentage")
    @Schema(description = "The percentage for how likely a player is to perform below their average")
    private double bustPercentage;
}
