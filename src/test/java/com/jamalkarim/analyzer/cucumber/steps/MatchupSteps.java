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

public class MatchupSteps extends BaseSteps {

    public MatchupSteps(ApiClient client, TestContext testContext, TestVariables testVariables, DbUtils dbUtils) {
        super(client, testContext, testVariables, dbUtils);
    }

    @When("^I run the player matchup for ([a-zA-Z\\d\\{\\}]+) and ([a-zA-Z\\d\\{\\}]+)$")
    public void createPlayerMatchup(String id1, String id2) {
        Response response = client.createPlayerMatchup(testVariables.resolve(id1), testVariables.resolve(id2));
        testContext.setResponse(response);

        if (response.getStatusCode() == 200) {
            PlayerMatchupResponseDTO playerMatchupResponseDTO = response.jsonPath().getObject("data", PlayerMatchupResponseDTO.class);
            testContext.setPlayerMatchupResponseDTO(playerMatchupResponseDTO);
        }

        response.prettyPrint();
    }

    @When("^I run the team matchup for ([a-zA-Z\\d\\{\\}]+) and ([a-zA-Z\\d\\{\\}]+)$")
    public void createTeamMatchup(String id1, String id2) {
        Response response = client.createTeamMatchup(testVariables.resolve(id1), testVariables.resolve(id2));
        testContext.setResponse(response);

        if (response.getStatusCode() == 200) {
            TeamMatchupResponseDTO teamMatchupResponseDTO = response.jsonPath().getObject("data", TeamMatchupResponseDTO.class);
            testContext.setTeamMatchupResponseDTO(teamMatchupResponseDTO);
        }

        response.prettyPrint();
    }

    @And("^I save the player matchup id to (\\{\\w+\\})")
    public void savePlayerMatchupId(String id) {
        testVariables.saveIdToVariable(id, testContext.getPlayerMatchupResponseDTO().getId());
    }

    @When("^I request the player matchup with id ([a-zA-Z\\d\\{\\}]+)$")
    public void getPlayerMatchupById(String id) {
        Response response = client.getPlayerMatchupById(testVariables.resolve(id));
        response.prettyPrint();
        testContext.setResponse(response);
        if (response.getStatusCode() == 200) {
            testContext.setPlayerMatchupResponseDTO(response.jsonPath().getObject("data", PlayerMatchupResponseDTO.class));
        }
    }

    @And("^I save the team matchup id to (\\{\\w+\\})")
    public void saveTeamMatchupId(String id) {
        testVariables.saveIdToVariable(id, testContext.getTeamMatchupResponseDTO().getId());
    }

    @When("^I request the team matchup with id ([a-zA-Z\\d\\{\\}]+)$")
    public void getTeamMatchupById(String id) {
        Response response = client.getTeamMatchupById(testVariables.resolve(id));
        response.prettyPrint();
        testContext.setResponse(response);
        if (response.getStatusCode() == 200) {
            testContext.setTeamMatchupResponseDTO(response.jsonPath().getObject("data", TeamMatchupResponseDTO.class));
        }
    }

    @Then("^the player matchup should be saved to the database$")
    public void verifyPlayerMatchupSaved() {
        PlayerMatchupResponseDTO playerMatchup = testContext.getPlayerMatchupResponseDTO();
        dbUtils.verifyPlayerMatchupIsSaved(playerMatchup.getId(), playerMatchup.getLoser(), playerMatchup.getWinner());
    }

    @Then("^the amount of player matchups saved to the database is (\\d+)$")
    public void verifyTeamMatchupSaved(String amount) {
        TeamMatchupResponseDTO teamMatchup = testContext.getTeamMatchupResponseDTO();
        dbUtils.verifyTeamMatchupIsSaved(teamMatchup.getId(), Integer.parseInt(amount));
    }
}
