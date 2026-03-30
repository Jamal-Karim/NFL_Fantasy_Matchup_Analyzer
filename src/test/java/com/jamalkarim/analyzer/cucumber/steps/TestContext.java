package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.dto.response.*;
import io.restassured.response.Response;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Shared context for a single Cucumber scenario.
 * Holds API responses and parsed DTOs to allow state sharing between steps.
 */
@Data
@Component
public class TestContext {
    /**
     * The last HTTP response received.
     */
    private Response response;

    /**
     * Parsed player details from the response.
     */
    private PlayerResponseDTO playerResponse;

    /**
     * Detailed Scare Factor analysis from the response.
     */
    private ScareResponseDTO scareResponse;

    private SimulationResponseDTO simulationResponse;

    /**
     * Parsed team details from the response.
     */
    private TeamResponseDTO teamResponse;

    /**
     * Result of a player-to-player matchup.
     */
    private PlayerMatchupResponseDTO playerMatchupResponse;

    /**
     * Result of a team-to-team matchup.
     */
    private TeamMatchupResponseDTO teamMatchupResponse;

    /**
     * Resets all fields to null.
     * Called before each scenario to ensure a clean state.
     */
    public void clear() {
        this.response = null;
        this.playerResponse = null;
        this.scareResponse = null;
        this.teamResponse = null;
        this.playerMatchupResponse = null;
        this.teamMatchupResponse = null;
    }
}
