package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.cucumber.utils.ApiClient;
import com.jamalkarim.analyzer.cucumber.utils.TestVariables;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

/**
 * Step definitions for player-related features.
 */
public class PlayerSteps {

    private final ApiClient client;
    private final TestContext testContext;
    private final TestVariables testVariables;

    public PlayerSteps(ApiClient client, TestContext testContext, TestVariables testVariables) {
        this.client = client;
        this.testContext = testContext;
        this.testVariables = testVariables;
    }

    @Given("^I fetch the player ([a-zA-Z\\s]+) on team ([A-Z]{2,3})$")
    public void fetchPlayer(String name, String nflTeam) {
        Response response = client.getPlayer(name, nflTeam);
        response.prettyPrint();
        PlayerResponseDTO playerDto = response.jsonPath().getObject("data", PlayerResponseDTO.class);
        testContext.setResponse(response);
        testVariables.addPlayerToMap(playerDto);
    }

    @When("^I request the Scare Factor for ([a-zA-Z\\s]+)")
    public void getScareFactor(String name) {
        PlayerResponseDTO playerDto = testVariables.getPlayer(name);
        long id = playerDto.getId();
        Response response = client.getScareFactor(id);
        response.prettyPrint();
    }
}
