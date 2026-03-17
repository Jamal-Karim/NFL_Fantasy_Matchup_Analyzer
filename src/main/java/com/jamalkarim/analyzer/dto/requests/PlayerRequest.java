package com.jamalkarim.analyzer.dto.requests;

import lombok.Data;

/**
 * Request DTO for a player.
 * Contains the name and NFL team of any given NFL player.
 */
@Data
public class PlayerRequest {
    private String name;
    private String team;
}
