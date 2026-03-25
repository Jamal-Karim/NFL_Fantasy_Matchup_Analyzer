package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.cucumber.utils.ApiClient;
import com.jamalkarim.analyzer.cucumber.utils.DbUtils;
import com.jamalkarim.analyzer.cucumber.utils.TestVariables;
import com.jamalkarim.analyzer.dto.response.PlayerMatchupResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamMatchupResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class MatchupSteps {

    private final ApiClient client;
    private final TestContext testContext;
    private final TestVariables testVariables;
    private final DbUtils dbUtils;

    public MatchupSteps(ApiClient client, TestContext testContext, TestVariables testVariables, DbUtils dbUtils) {
        this.client = client;
        this.testContext = testContext;
        this.testVariables = testVariables;
        this.dbUtils = dbUtils;
    }

    @When("^I run the player matchup for (\\{\\w+\\}) and (\\{\\w+\\})$")
    public void createPlayerMatchup(String id1, String id2) {
        Response response = client.createPlayerMatchup(testVariables.getKey(id1).toString(), testVariables.getKey(id2).toString());
        testContext.setResponse(response);

        if (response.getStatusCode() == 200) {
            PlayerMatchupResponseDTO playerMatchupResponseDTO = response.jsonPath().getObject("data", PlayerMatchupResponseDTO.class);
            testContext.setPlayerMatchupResponseDTO(playerMatchupResponseDTO);
        }

        response.prettyPrint();
    }

    @When("^I run the team matchup for (\\{\\w+\\}) and (\\{\\w+\\})$")
    public void createTeamMatchup(String id1, String id2) {
        Response response = client.createTeamMatchup(testVariables.getKey(id1).toString(), testVariables.getKey(id2).toString());
        testContext.setResponse(response);

        if (response.getStatusCode() == 200) {
            TeamMatchupResponseDTO teamMatchupResponseDTO = response.jsonPath().getObject("data", TeamMatchupResponseDTO.class);
            testContext.setTeamMatchupResponseDTO(teamMatchupResponseDTO);
        }

        response.prettyPrint();
    }

    @And("^I save the player matchup id to (\\{\\w+\\})")
    public void savePlayerMatchupId(String id) {
        testVariables.fillSafely(id, testContext.getPlayerMatchupResponseDTO().getId());
    }

    @When("^I request the player matchup with id (\\{\\w+\\})$")
    public void getPlayerMatchupFromSavedId(String id) {
        Response response = client.getPlayerMatchupById(String.valueOf(testVariables.getKey(id)));
        response.prettyPrint();
        testContext.setResponse(response);
        testContext.setPlayerMatchupResponseDTO(response.jsonPath().getObject("data", PlayerMatchupResponseDTO.class));
    }

    @When("^I request the player matchup with id ([a-zA-Z\\d]+)$")
    public void getPlayerMatchupFromId(String id) {
        Response response = client.getPlayerMatchupById(id);
        response.prettyPrint();
        testContext.setResponse(response);
        if (response.getStatusCode() == 200) {
            testContext.setPlayerMatchupResponseDTO(response.jsonPath().getObject("data", PlayerMatchupResponseDTO.class));
        }
    }

    @And("^I save the team matchup id to (\\{\\w+\\})")
    public void saveTeamMatchupId(String id) {
        testVariables.fillSafely(id, testContext.getTeamMatchupResponseDTO().getId());
    }

    @When("^I request the team matchup with id (\\{\\w+\\})$")
    public void getTeamMatchupFromSavedId(String id) {
        Response response = client.getTeamMatchupById(String.valueOf(testVariables.getKey(id)));
        response.prettyPrint();
        testContext.setResponse(response);
        testContext.setTeamMatchupResponseDTO(response.jsonPath().getObject("data", TeamMatchupResponseDTO.class));
    }

    @When("^I request the team matchup with id ([a-zA-Z\\d]+)$")
    public void getTeamMatchupFromId(String id) {
        Response response = client.getTeamMatchupById(id);
        response.prettyPrint();
        testContext.setResponse(response);
        if (response.getStatusCode() == 200) {
            testContext.setTeamMatchupResponseDTO(response.jsonPath().getObject("data", TeamMatchupResponseDTO.class));
        }
    }

}
