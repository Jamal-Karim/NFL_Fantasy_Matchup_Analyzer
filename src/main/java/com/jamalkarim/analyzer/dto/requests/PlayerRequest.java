package com.jamalkarim.analyzer.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Request DTO for a player.
 * Contains the name and NFL team of any given NFL player.
 */
@Data
@Schema(description = "Request payload for identifying an NFL player for synchronization or roster assignment")
public class PlayerRequest {
    @Schema(description = "Full name of the player", example = "Josh Allen", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "NFL team abbreviation", example = "BUF", requiredMode = Schema.RequiredMode.REQUIRED)
    private String team;
}
