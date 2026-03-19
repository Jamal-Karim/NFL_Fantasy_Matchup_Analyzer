package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jamalkarim.analyzer.domain.enums.MatchupAdvantages;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({
        "id",
        "team_1",
        "team_2",
        "team_1_score",
        "team_2_score",
        "team_1_win_probability",
        "team_2_win_probability",
        "advantage",
        "player_matchups"
})
public class TeamMatchupResponseDTO {

    private long id;

    @JsonProperty("team_1")
    private String team1;

    @JsonProperty("team_2")
    private String team2;

    @JsonProperty("team_1_score")
    private double team1TotalScore;

    @JsonProperty("team_2_score")
    private double team2TotalScore;

    @JsonProperty("team_1_win_probability")
    private double team1Probability;

    @JsonProperty("team_2_win_probability")
    private double team2Probability;

    private MatchupAdvantages advantage;

    @JsonProperty("player_matchups")
    private List<PlayerMatchupResponseForTeamDTO> playerMatchupResponses;
}
