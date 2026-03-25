package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.cucumber.utils.ApiClient;
import com.jamalkarim.analyzer.cucumber.utils.DbUtils;
import com.jamalkarim.analyzer.cucumber.utils.TestVariables;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for player-related features.
 */
public class PlayerSteps {

    private final ApiClient client;
    private final TestContext testContext;
    private final TestVariables testVariables;
    private final DbUtils dbUtils;

    public PlayerSteps(ApiClient client, TestContext testContext, TestVariables testVariables, DbUtils dbUtils) {
        this.client = client;
        this.testContext = testContext;
        this.testVariables = testVariables;
        this.dbUtils = dbUtils;
    }

    @Given("^I fetch the player ([a-zA-Z\\s]+) on team ([A-Z]{2,3})$")
    public void fetchPlayer(String name, String nflTeam) {
        Response response = client.getPlayer(name, nflTeam);
        response.prettyPrint();
        PlayerResponseDTO playerDto = response.jsonPath().getObject("data", PlayerResponseDTO.class);
        testContext.setResponse(response);
        testContext.setPlayerResponse(playerDto);
        testVariables.addPlayerToMap(playerDto);
    }

    @When("^I request the Scare Factor for ([a-zA-Z\\s]+)")
    public void getScareFactor(String name) {
        PlayerResponseDTO playerDto = testVariables.getPlayer(name);
        long id = playerDto.getId();
        Response response = client.getScareFactor(id);
        response.prettyPrint();
        testContext.setScareResponse(response.jsonPath().getObject("data", ScareResponseDTO.class));
    }

    @Then("^the scare factor should be greater than ([0-9]+)$")
    public void verifyScareFactor(String scareFactor) {
        assertThat(testContext.getScareResponse().getScareScore()).isGreaterThan(Integer.parseInt(scareFactor));
    }

    @Then("^the player should be saved to the database$")
    public void verifyPlayerSavedToDB() {
        dbUtils.verifyPlayerIsSaved(testContext.getPlayerResponse().getName());
    }
}
