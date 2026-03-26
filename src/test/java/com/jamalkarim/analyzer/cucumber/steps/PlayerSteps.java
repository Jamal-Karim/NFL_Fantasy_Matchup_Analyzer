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

/**
 * Step definitions for Player-related operations.
 * Handles fetching, syncing, and analyzing player data and Scare Factors.
 */
public class PlayerSteps extends BaseSteps {

    public PlayerSteps(ApiClient client, TestContext testContext, TestVariables testVariables, DbUtils dbUtils) {
        super(client, testContext, testVariables, dbUtils);
    }

    /**
     * Fetches or syncs a single player by name and NFL team.
     */
    @Given("^I fetch the player ([a-zA-Z\\s]+) on team ([A-Z]{2,3})$")
    public void fetchSinglePlayer(String name, String nflTeam) {
        fetchPlayer(name, nflTeam);
    }

    /**
     * Fetches or syncs multiple players using a data table.
     */
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

    /**
     * Internal helper to fetch a player and update shared state.
     */
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

    /**
     * Requests a player's details by their database ID.
     */
    @When("^I request the player with id ([a-zA-Z\\d\\{\\}]+)$")
    public void getPlayerById(String id) {
        Response response = client.getPlayerById(testVariables.resolve(id));
        response.prettyPrint();
        testContext.setResponse(response);
        if (response.getStatusCode() == 200) {
            testContext.setPlayerResponse(response.jsonPath().getObject("data", PlayerResponseDTO.class));
        }
    }

    /**
     * Requests the Scare Factor analysis for a player by their name.
     */
    @When("^I request the Scare Factor for ((?!player with id)[a-zA-Z\\s]+)$")
    public void getScareFactorFromName(String name) {
        PlayerResponseDTO playerDto = testVariables.getPlayer(name);
        long id = playerDto.getId();
        Response response = client.getScareFactor(String.valueOf(id));
        response.prettyPrint();
        testContext.setResponse(response);
        testContext.setScareResponse(response.jsonPath().getObject("data", ScareResponseDTO.class));
    }

    /**
     * Requests the Scare Factor analysis for a player by their database ID.
     */
    @When("^I request the Scare Factor for player with id ([a-zA-Z\\d\\{\\}]+)$")
    public void getScareFactorById(String id) {
        Response response = client.getScareFactor(testVariables.resolve(id));
        response.prettyPrint();
        testContext.setResponse(response);
        if (response.getStatusCode() == 200) {
            testContext.setScareResponse(response.jsonPath().getObject("data", ScareResponseDTO.class));
        }
    }

    /**
     * Asserts that the player's Scare Score is greater than a specified threshold.
     */
    @Then("^the scare factor should be greater than ([0-9]+)$")
    public void verifyScareFactor(String scareFactor) {
        assertThat(testContext.getScareResponse().getScareScore()).isGreaterThan(Integer.parseInt(scareFactor));
    }

    /**
     * Verifies that the player record exists in the database.
     */
    @Then("^the player should be saved to the database$")
    public void verifyPlayerSavedToDB() {
        dbUtils.verifyPlayerIsSaved(testContext.getPlayerResponse().getName());
    }

    /**
     * Verifies that the player's statistical entities are persisted in the database.
     */
    @And("^the player stats should be saved to the database$")
    public void verifyPlayerStatsSavedToDB() {
        dbUtils.verifyPlayerStatsAreSaved(testContext.getPlayerResponse().getName());
    }

    /**
     * Verifies that the analysis result is persisted in the database.
     */
    @Then("^the scare score should be saved to the database$")
    public void verifyScareScoreSavedToDB() {
        dbUtils.verifyScareResultIsSaved(testContext.getPlayerResponse().getId());
    }

    /**
     * Verifies that the player's Scare Tier matches the expected value.
     */
    @And("^the scare tier should be ([A-Z]+)$")
    public void verifyScareTier(String expectedTier) {
        assertThat(testContext.getScareResponse().getScareTier().name()).isEqualTo(expectedTier);
    }

    /**
     * Verifies that the analysis reasoning contains specific text.
     */
    @And("^the scare analysis should contain explanation: \"([^\"]+)\"$")
    public void verifyScareExplanation(String expectedExplanation) {
        String primary = testContext.getScareResponse().getPrimaryExplanation();
        List<String> supporting = testContext.getScareResponse().getSupportingExplanations();
        
        boolean foundInPrimary = primary != null && primary.contains(expectedExplanation);
        boolean foundInSupporting = supporting.stream().anyMatch(e -> e.contains(expectedExplanation));
        
        assertThat(foundInPrimary || foundInSupporting)
                .withFailMessage("Explanation [%s] not found in primary or supporting explanations.", expectedExplanation)
                .isTrue();
    }

    /**
     * Saves the current player's ID to a variable.
     */
    @And("^the player id is saved to (\\{\\w+\\})$")
    public void savePlayerId(String id) {
        testVariables.saveIdToVariable(id, testContext.getPlayerResponse().getId());
    }

    /**
     * Fetches all players from the system.
     */
    @When("^I get all players$")
    public void getAllPlayers() {
        Response response = client.getAllPlayers(null, null, null);
        testContext.setResponse(response);
        response.prettyPrint();
    }

    /**
     * Fetches a paginated list of players.
     */
    @When("^I get all players with page (\\d+) and size (\\d+)$")
    public void getAllPlayersPaginated(int page, int size) {
        Response response = client.getAllPlayers(null, page, size);
        testContext.setResponse(response);
        response.prettyPrint();
    }

    /**
     * Fetches players filtered by a specific position.
     */
    @When("^I get all players with position ([A-Z]+)$")
    public void getAllPlayersByPosition(String position) {
        Response response = client.getAllPlayers(position, null, null);
        testContext.setResponse(response);
        response.prettyPrint();
    }

    /**
     * Verifies that the player list is returned in descending order of Scare Factor.
     */
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

    /**
     * Verifies if a player is (or is not) assigned to a specific fantasy team in the database.
     */
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
