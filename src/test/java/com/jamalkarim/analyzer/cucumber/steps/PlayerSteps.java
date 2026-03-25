package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.cucumber.utils.ApiClient;
import com.jamalkarim.analyzer.cucumber.utils.DbUtils;
import com.jamalkarim.analyzer.cucumber.utils.TestVariables;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void fetchSinglePlayer(String name, String nflTeam) {
        fetchPlayer(name, nflTeam);
    }

    @Given("^I fetch the players:$")
    public void fetchMultiplePlayers(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);

        for (Map<String, String> cols : rows) {
            String name = cols.get("name");
            String nflTeam = cols.get("nfl_team");
            fetchPlayer(name, nflTeam);
        }
    }

    private void fetchPlayer(String name, String nflTeam) {
        Response response = client.getPlayer(name, nflTeam);
        testContext.setResponse(response);

        if (response.getStatusCode() == 200) {
            PlayerResponseDTO playerDto = response.jsonPath().getObject("data", PlayerResponseDTO.class);

            if (playerDto != null) {
                testContext.setPlayerResponse(playerDto);
                testVariables.addPlayerToMap(playerDto);
            }
        }

        response.prettyPrint();
    }

    @When("^I request the Scare Factor for ([a-zA-Z\\s]+)$")
    public void getScareFactorFromName(String name) {
        PlayerResponseDTO playerDto = testVariables.getPlayer(name);
        long id = playerDto.getId();
        Response response = client.getScareFactor(id);
        response.prettyPrint();
        testContext.setResponse(response);
        testContext.setScareResponse(response.jsonPath().getObject("data", ScareResponseDTO.class));
    }

    @When("^I request the Scare Factor for player with id (\\{\\w+\\})$")
    public void getScareFactorFromSavedId(String id) {
        Response response = client.getScareFactor((Long) testVariables.getKey(id));
        response.prettyPrint();
        testContext.setResponse(response);
        testContext.setScareResponse(response.jsonPath().getObject("data", ScareResponseDTO.class));
    }

    @When("^I request the Scare Factor for player with id (\\d+)$")
    public void getScareFactorFromId(String id) {
        Response response = client.getScareFactor(Long.parseLong(id));
        response.prettyPrint();
        testContext.setResponse(response);
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

    @Then("^the scare score should be saved to the database$")
    public void verifyScareScoreSavedToDB() {
        dbUtils.verifyScareResultIsSaved(testContext.getPlayerResponse().getId());
    }

    @And("^the player id is saved to (\\{\\w+\\})$")
    public void savePlayerId(String id) {
        testVariables.fillSafely(id, testContext.getPlayerResponse().getId());
    }

    @When("^I get all players$")
    public void getAllPlayers() {
        Response response = client.getAllPlayers();
        testContext.setResponse(response);
        response.prettyPrint();
    }

    @When("^I get all players with position ([A-Z]+)$")
    public void getAllPlayersByPosition(String position) {
        Response response = client.getAllPlayersByPosition(position);
        testContext.setResponse(response);
        response.prettyPrint();
    }

    @And("the players are sorted by Scare Factor descending")
    public void verifyPlayersSortedByScareFactorDescending() {
        List<PlayerResponseDTO> listOfPlayers = testContext.getResponse().jsonPath()
                .getList("data.content", PlayerResponseDTO.class);

        double maxScareScore = 101;

        for (PlayerResponseDTO player : listOfPlayers) {
            Response response = client.getScareFactor(player.getId());
            double scareFactor = response.jsonPath().getObject("data", ScareResponseDTO.class).getScareScore();
            assertThat(scareFactor).isLessThanOrEqualTo(maxScareScore);
            maxScareScore = scareFactor;
        }
    }
}
