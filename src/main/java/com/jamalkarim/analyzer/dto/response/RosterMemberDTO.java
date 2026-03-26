package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jamalkarim.analyzer.domain.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer Object representing a simplified member of a team roster.
 */
@Data
@JsonPropertyOrder({"id", "position", "name", "nflTeam"})
@Schema(description = "Simplified player data for roster display")
public class RosterMemberDTO {

    @Schema(description = "Internal database ID of the player", example = "101")
    private long id;

    @Schema(description = "Full name of the player", example = "Josh Allen")
    private String name;

    @JsonProperty("nfl_team")
    @Schema(description = "NFL team abbreviation", example = "BUF")
    private String nflTeam;

    @Schema(description = "Player position", example = "QB")
    private Position position;
}
