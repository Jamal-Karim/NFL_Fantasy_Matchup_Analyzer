package com.jamalkarim.analyzer.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Request DTO for initiating a team creation.
 * Contains the name of the team and a list of player requests.
 */
@Data
@Schema(description = "Request payload for creating or updating a fantasy football team")
public class TeamRequest {
    @Schema(description = "Desired name for the fantasy team", example = "The Gridiron Gurus", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "List of players to be included in the initial roster")
    private List<PlayerRequest> roster;
}
