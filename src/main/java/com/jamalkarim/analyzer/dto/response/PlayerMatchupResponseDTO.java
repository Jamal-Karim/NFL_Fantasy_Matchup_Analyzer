package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jamalkarim.analyzer.domain.enums.MatchupAdvantages;
import lombok.Data;

/**
 * Data Transfer Object representing the results of a player matchup analysis.
 * Includes both players' detailed stats and the calculated advantage.
 */
@Data
@JsonPropertyOrder({
        "id",
        "winner",
        "loser",
        "scare_difference",
        "advantage",
        "explanation",
        "player_1_scare_result",
        "player_2_scare_result"
})
public class PlayerMatchupResponseDTO {

    private Long id;

    private String winner;
    private String loser;

    @JsonProperty("scare_difference")
    private double scareDifference;

    private MatchupAdvantages advantage;
    private String explanation;

    @JsonProperty("player_1_scare_result")
    private ScareResponseDTO player1ScareResult;

    @JsonProperty("player_2_scare_result")
    private ScareResponseDTO player2ScareResult;
}
