package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.cucumber.utils.ApiClient;
import com.jamalkarim.analyzer.cucumber.utils.DbUtils;
import com.jamalkarim.analyzer.cucumber.utils.TestVariables;
import com.jamalkarim.analyzer.dto.requests.PlayerRequest;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Step definitions for Fantasy Team management.
 * Handles team creation, updates, deletion, and DB persistence checks.
 */
public class TeamSteps extends BaseSteps {

    public TeamSteps(ApiClient client, TestContext testContext, TestVariables testVariables, DbUtils dbUtils) {
        super(client, testContext, testVariables, dbUtils);
    }

    /**
     * Creates a new fantasy team with a specific name and roster.
     */
    @Given("^I create a fantasy team ([a-zA-Z0-9\\s]+):$")
    public void createTeam(String fantasyTeamName, DataTable table) {

        Response response = client.createTeam(fantasyTeamName, createRosterFromTable(table));
        testContext.setResponse(response);

        if (response.getStatusCode() == 200) {
            TeamResponseDTO teamResponseDTO = response.jsonPath().getObject("data", TeamResponseDTO.class);
            testContext.setTeamResponseDTO(teamResponseDTO);
        }

        response.prettyPrint();
    }

    /**
     * Updates an existing team's name or roster.
     */
    @When("^I update the team ([a-zA-Z\\d\\{\\}]+)(?: to ([a-zA-Z0-9\\s]+))?:$")
    public void updateTeam(String id, String newName, DataTable table) {

        String resolvedId = testVariables.resolve(id);
        TeamResponseDTO team = client.getTeamById(resolvedId)
                .jsonPath().getObject("data", TeamResponseDTO.class);

        String finalName = (newName != null) ? newName : team.getName();

        Response response = client.updateTeam(resolvedId,
                finalName, createRosterFromTable(table));

        if (response.getStatusCode() == 200) {
            TeamResponseDTO teamResponseDTO = response.jsonPath().getObject("data", TeamResponseDTO.class);
            testContext.setTeamResponseDTO(teamResponseDTO);
        }

        response.prettyPrint();
    }

    /**
     * Helper to transform a Gherkin data table into a list of PlayerRequest objects.
     */
    private List<PlayerRequest> createRosterFromTable(DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);

        List<PlayerRequest> roster = new ArrayList<>();

        for (Map<String, String> cols : rows) {
            String name = cols.get("name");
            String nflTeam = cols.get("team");

            PlayerRequest playerRequest = new PlayerRequest();
            playerRequest.setName(name);
            playerRequest.setTeam(nflTeam);

            roster.add(playerRequest);
        }

        return roster;
    }

    /**
     * Saves the current team's database ID to a variable.
     */
    @And("^the team id is saved to (\\{\\w+\\})$")
    public void saveTeamId(String id) {
        testVariables.saveIdToVariable(id, testContext.getTeamResponseDTO().getId());
    }

    /**
     * Verifies that the team record exists in the database.
     */
    @Then("^the team should be saved to the database$")
    public void verifyTeamSavedToDB() {
        dbUtils.verifyTeamIsSaved(testContext.getTeamResponseDTO().getName());
    }

    /**
     * Verifies that a specific team record no longer exists in the database.
     */
    @And("^the team \"([^\"]+)\" should not exist in the database$")
    public void verifyTeamDeleted(String teamName) {
        dbUtils.verifyTeamDoesNotExist(teamName);
    }

    /**
     * Fetches a team's details by its ID.
     */
    @When("^I request the team with id ([a-zA-Z\\d\\{\\}]+)$")
    public void getTeamById(String id) {
        Response response = client.getTeamById(testVariables.resolve(id));
        response.prettyPrint();
        testContext.setResponse(response);
        if (response.getStatusCode() == 200) {
            testContext.setTeamResponseDTO(response.jsonPath().getObject("data", TeamResponseDTO.class));
        }
    }

    /**
     * Fetches a list of all fantasy teams.
     */
    @When("^I get all teams$")
    public void getAllTeams() {
        Response response = client.getAlLTeams();
        testContext.setResponse(response);
        response.prettyPrint();
    }

    /**
     * Deletes a team by its ID.
     */
    @When("^I delete the team with id ([a-zA-Z\\d\\{\\}]+)$")
    public void deleteTeamById(String id) {
        Response response = client.deleteTeam(testVariables.resolve(id));
        response.prettyPrint();
        testContext.setResponse(response);
    }
}
