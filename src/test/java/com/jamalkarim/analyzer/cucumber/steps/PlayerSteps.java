package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.cucumber.utils.ApiClient;
import com.jamalkarim.analyzer.cucumber.utils.DbUtils;
import com.jamalkarim.analyzer.cucumber.utils.TestVariables;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.ScareResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerSteps extends BaseSteps {

    public PlayerSteps(ApiClient client, TestContext testContext, TestVariables testVariables, DbUtils dbUtils) {
        super(client, testContext, testVariables, dbUtils);
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

            if (cols.containsKey("variable")) {
                String variable = cols.get("variable");
                testVariables.saveIdToVariable(variable, testContext.getPlayerResponse().getId());
            }
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

    @When("^I request the player with id ([a-zA-Z\\d\\{\\}]+)$")
    public void getPlayerById(String id) {
        Response response = client.getPlayerById(testVariables.resolve(id));
        response.prettyPrint();
        testContext.setResponse(response);
        if (response.getStatusCode() == 200) {
            testContext.setPlayerResponse(response.jsonPath().getObject("data", PlayerResponseDTO.class));
        }
    }

    @When("^I request the Scare Factor for ((?!player with id)[a-zA-Z\\s]+)$")
    public void getScareFactorFromName(String name) {
        PlayerResponseDTO playerDto = testVariables.getPlayer(name);
        long id = playerDto.getId();
        Response response = client.getScareFactor(String.valueOf(id));
        response.prettyPrint();
        testContext.setResponse(response);
        testContext.setScareResponse(response.jsonPath().getObject("data", ScareResponseDTO.class));
    }

    @When("^I request the Scare Factor for player with id ([a-zA-Z\\d\\{\\}]+)$")
    public void getScareFactorById(String id) {
        Response response = client.getScareFactor(testVariables.resolve(id));
        response.prettyPrint();
        testContext.setResponse(response);
        if (response.getStatusCode() == 200) {
            testContext.setScareResponse(response.jsonPath().getObject("data", ScareResponseDTO.class));
        }
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
        testVariables.saveIdToVariable(id, testContext.getPlayerResponse().getId());
    }

    @When("^I get all players$")
    public void getAllPlayers() {
        Response response = client.getAllPlayers();
        testContext.setResponse(response);
        response.prettyPrint();
    }

    @When("^I get all players with page (\\d+) and size (\\d+)$")
    public void getAllPlayersPaginated(int page, int size) {
        Response response = client.getAllPlayers(page, size);
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
            Response response = client.getScareFactor(String.valueOf(player.getId()));
            double scareFactor = response.jsonPath().getObject("data", ScareResponseDTO.class).getScareScore();
            assertThat(scareFactor).isLessThanOrEqualTo(maxScareScore);
            maxScareScore = scareFactor;
        }
    }

    @Then("^([a-zA-Z\\s]+) should (not )?be on team ([a-zA-Z\\d\\{\\}]+)$")
    public void verifyPlayerIsOnTeam(String playerName, String not, String key) {

        String teamId = testVariables.resolve(key);
        boolean shouldBePresent = (not == null);
        String teamName = "";

        if (shouldBePresent) {
            Response response = client.getTeamById(teamId);
            teamName = response.jsonPath().getObject("data", TeamResponseDTO.class).getName();
        } else {
            teamName = "DELETED_TEAM";
        }

        dbUtils.verifyPlayerIsOnTeam(playerName, teamName, shouldBePresent);
    }
}
