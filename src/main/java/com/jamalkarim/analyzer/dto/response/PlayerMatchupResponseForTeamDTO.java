package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jamalkarim.analyzer.domain.enums.MatchupAdvantages;
import lombok.Data;

/**
 * Data Transfer Object representing a simplified player matchup result,
 * specifically for inclusion within a team matchup report.
 */
@Data
@JsonPropertyOrder({
        "id",
        "winner",
        "loser",
        "scare_difference",
        "advantage"
})
public class PlayerMatchupResponseForTeamDTO {
    private Long id;

    private String winner;
    private String loser;

    @JsonProperty("scare_difference")
    private double scareDifference;

    private MatchupAdvantages advantage;
}
