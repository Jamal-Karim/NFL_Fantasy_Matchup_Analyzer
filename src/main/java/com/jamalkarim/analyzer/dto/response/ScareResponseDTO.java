package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import com.jamalkarim.analyzer.domain.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object for detailed Scare Factor analysis results.
 * Contains the score, tier, and descriptive reasons behind the analysis.
 */
@Data
@JsonPropertyOrder({
        "name",
        "nfl_team",
        "position",
        "scare_score",
        "scare_tier",
        "primary_explanation",
        "supporting_explanations"
})
@Schema(description = "Analytical output containing the proprietary 'Scare Factor' evaluation for a specific player matchup")
public class ScareResponseDTO {

    @Schema(description = "Full name of the analyzed player", example = "Josh Allen")
    private String name;

    @JsonProperty("nfl_team")
    @Schema(description = "NFL team abbreviation", example = "BUF")
    private String team;

    @Schema(description = "Player position", example = "QB")
    private Position position;

    @JsonProperty("scare_score")
    @Schema(description = "Numerical performance risk score (0-100), where higher indicates better projected performance", example = "85.5")
    private double scareScore;

    @JsonProperty("scare_tier")
    @Schema(description = "Categorical performance tier based on the Scare Score", example = "ELITE")
    private PlayerTier scareTier;

    @JsonProperty("primary_explanation")
    @Schema(description = "High-level summary of the analysis results")
    private String primaryExplanation;

    @JsonProperty("supporting_explanations")
    @Schema(description = "Detailed list of contributing factors (e.g., historical stats, opponent defense rank)")
    private List<String> supportingExplanations;
}
