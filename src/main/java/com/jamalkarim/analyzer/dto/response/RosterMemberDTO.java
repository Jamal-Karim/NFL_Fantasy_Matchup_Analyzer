package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jamalkarim.analyzer.domain.enums.Position;
import lombok.Data;

/**
 * Data Transfer Object representing a simplified member of a team roster.
 */
@Data
@JsonPropertyOrder({"id", "position", "name", "nflTeam"})
public class RosterMemberDTO {

    private long id;

    private String name;

    @JsonProperty("nfl_team")
    private String nflTeam;

    private Position position;
}
