package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.cucumber.utils.ApiClient;
import com.jamalkarim.analyzer.cucumber.utils.DbUtils;
import com.jamalkarim.analyzer.cucumber.utils.TestVariables;
import com.jamalkarim.analyzer.dto.response.PlayerMatchupResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamMatchupResponseDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

/**
 * Step definitions for head-to-head matchup operations.
 * Handles both player-to-player and team-to-team analysis scenarios.
 */
public class MatchupSteps extends BaseSteps {

    public MatchupSteps(ApiClient client, TestContext testContext, TestVariables testVariables, DbUtils dbUtils) {
        super(client, testContext, testVariables, dbUtils);
    }

    /**
     * Triggers a head-to-head matchup analysis between two players.
     *
     * @param id1 The first player's ID or variable
     * @param id2 The second player's ID or variable
     */
    @When("^I run the player matchup for ([a-zA-Z\\d\\{\\}]+) and ([a-zA-Z\\d\\{\\}]+)$")
    public void createPlayerMatchup(String id1, String id2) {
        Response response = client.createPlayerMatchup(testVariables.resolve(id1), testVariables.resolve(id2));
        testContext.setResponse(response);

        if (response.getStatusCode() == 200) {
            PlayerMatchupResponseDTO playerMatchupResponseDTO = response.jsonPath().getObject("data", PlayerMatchupResponseDTO.class);
            testContext.setPlayerMatchupResponse(playerMatchupResponseDTO);
        }

        response.prettyPrint();
    }

    /**
     * Triggers a head-to-head matchup analysis between two fantasy teams.
     *
     * @param id1 The first team's ID or variable
     * @param id2 The second team's ID or variable
     */
    @When("^I run the team matchup for ([a-zA-Z\\d\\{\\}]+) and ([a-zA-Z\\d\\{\\}]+)$")
    public void createTeamMatchup(String id1, String id2) {
        Response response = client.createTeamMatchup(testVariables.resolve(id1), testVariables.resolve(id2));
        testContext.setResponse(response);

        if (response.getStatusCode() == 200) {
            TeamMatchupResponseDTO teamMatchupResponseDTO = response.jsonPath().getObject("data", TeamMatchupResponseDTO.class);
            testContext.setTeamMatchupResponse(teamMatchupResponseDTO);
        }

        response.prettyPrint();
    }

    /**
     * Saves the last generated player matchup ID to a variable.
     */
    @And("^I save the player matchup id to (\\{\\w+\\})")
    public void savePlayerMatchupId(String id) {
        testVariables.saveIdToVariable(id, testContext.getPlayerMatchupResponse().getId());
    }

    /**
     * Fetches a player matchup report by ID.
     */
    @When("^I request the player matchup with id ([a-zA-Z\\d\\{\\}]+)$")
    public void getPlayerMatchupById(String id) {
        Response response = client.getPlayerMatchupById(testVariables.resolve(id));
        response.prettyPrint();
        testContext.setResponse(response);
        if (response.getStatusCode() == 200) {
            testContext.setPlayerMatchupResponse(response.jsonPath().getObject("data", PlayerMatchupResponseDTO.class));
        }
    }

    /**
     * Saves the last generated team matchup ID to a variable.
     */
    @And("^I save the team matchup id to (\\{\\w+\\})")
    public void saveTeamMatchupId(String id) {
        testVariables.saveIdToVariable(id, testContext.getTeamMatchupResponse().getId());
    }

    /**
     * Fetches a team matchup report by ID.
     */
    @When("^I request the team matchup with id ([a-zA-Z\\d\\{\\}]+)$")
    public void getTeamMatchupById(String id) {
        Response response = client.getTeamMatchupById(testVariables.resolve(id));
        response.prettyPrint();
        testContext.setResponse(response);
        if (response.getStatusCode() == 200) {
            testContext.setTeamMatchupResponse(response.jsonPath().getObject("data", TeamMatchupResponseDTO.class));
        }
    }

    /**
     * Verifies that the player matchup result is correctly persisted in the database.
     */
    @Then("^the player matchup should be saved to the database$")
    public void verifyPlayerMatchupSaved() {
        PlayerMatchupResponseDTO playerMatchup = testContext.getPlayerMatchupResponse();
        dbUtils.verifyPlayerMatchupIsSaved(playerMatchup.getId(), playerMatchup.getLoser(), playerMatchup.getWinner());
    }

    /**
     * Verifies the count of player battles within a team matchup result in the database.
     */
    @Then("^the amount of player matchups saved to the database is (\\d+)$")
    public void verifyTeamMatchupSaved(String amount) {
        TeamMatchupResponseDTO teamMatchup = testContext.getTeamMatchupResponse();
        dbUtils.verifyTeamMatchupIsSaved(teamMatchup.getId(), Integer.parseInt(amount));
    }
}
