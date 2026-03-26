package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object representing the response for team-related API requests.
 */
@Data
@JsonPropertyOrder({"id", "name", "roster"})
@Schema(description = "Data for a managed fantasy football team and its current roster")
public class TeamResponseDTO {
    @Schema(description = "Internal database ID of the fantasy team", example = "50")
    private long id;

    @Schema(description = "User-defined name of the fantasy team", example = "The Gridiron team")
    private String name;

    @Schema(description = "List of players currently assigned to this team's roster")
    private List<RosterMemberDTO> roster;
}
