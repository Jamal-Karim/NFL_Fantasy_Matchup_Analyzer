package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jamalkarim.analyzer.domain.enums.PlayerTier;
import com.jamalkarim.analyzer.domain.enums.Position;
import lombok.Data;

import java.util.List;

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
public class ScareResponseDTO {

    private String name;

    @JsonProperty("nfl_team")
    private String team;

    private Position position;

    @JsonProperty("scare_score")
    private double scareScore;

    @JsonProperty("scare_tier")
    private PlayerTier scareTier;

    @JsonProperty("primary_explanation")
    private String primaryExplanation;

    @JsonProperty("supporting_explanations")
    private List<String> supportingExplanations;
}
