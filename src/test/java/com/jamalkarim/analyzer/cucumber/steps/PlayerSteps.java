package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.cucumber.utils.ApiClient;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;

/**
 * Step definitions for player-related features.
 */
public class PlayerSteps {

    private final ApiClient client;
    private final TestContext testContext;

    public PlayerSteps(ApiClient client, TestContext testContext) {
        this.client = client;
        this.testContext = testContext;
    }

    @Given("^I fetch the player ([a-zA-Z\\s]+) on team ([A-Z]{2,3})$")
    public void fetchPlayer(String name, String nflTeam) {
        Response response = client.getPlayer(name, nflTeam);
        response.prettyPrint();
        testContext.setResponse(response);
    }
}
