package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.cucumber.utils.ApiClient;
import com.jamalkarim.analyzer.cucumber.utils.DbUtils;
import com.jamalkarim.analyzer.cucumber.utils.TestVariables;
import com.jamalkarim.analyzer.dto.requests.PlayerRequest;
import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import com.jamalkarim.analyzer.dto.response.PlayerResponseDTO;
import com.jamalkarim.analyzer.dto.response.TeamResponseDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeamSteps {

    private final ApiClient client;
    private final TestContext testContext;
    private final TestVariables testVariables;
    private final DbUtils dbUtils;

    public TeamSteps(ApiClient client, TestContext testContext, TestVariables testVariables, DbUtils dbUtils) {
        this.client = client;
        this.testContext = testContext;
        this.testVariables = testVariables;
        this.dbUtils = dbUtils;
    }

    @Given("^I create a fantasy team ([a-zA-Z0-9\\s]+):$")
    public void createTeam(String fantasyTeamName, DataTable table) {
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

        Response response = client.createTeam(fantasyTeamName, roster);
        testContext.setResponse(response);

        if (response.getStatusCode() == 200) {
            TeamResponseDTO teamResponseDTO = response.jsonPath().getObject("data", TeamResponseDTO.class);
            testContext.setTeamResponseDTO(teamResponseDTO);
        }

        response.prettyPrint();
    }

    @And("^the team id is saved to (\\{\\w+\\})$")
    public void saveTeamId(String id) {
        testVariables.fillSafely(id, testContext.getTeamResponseDTO().getId());
    }
}
