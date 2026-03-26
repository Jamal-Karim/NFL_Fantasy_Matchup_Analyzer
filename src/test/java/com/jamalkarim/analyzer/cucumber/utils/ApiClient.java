package com.jamalkarim.analyzer.cucumber.utils;

import com.jamalkarim.analyzer.dto.requests.PlayerMatchupRequest;
import com.jamalkarim.analyzer.dto.requests.PlayerRequest;
import com.jamalkarim.analyzer.dto.requests.TeamMatchupRequest;
import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * REST API Client for Cucumber tests.
 * Centralizes all API calls and handles request configuration, such as logging and content types.
 */
@Component
public class ApiClient {

    private static final String PLAYER_BASE = "/api/player";
    private static final String TEAM_BASE = "/api/team";
    private static final String MATCHUP_BASE = "/api/matchup";

    /**
     * Creates a base request specification with standard logging.
     */
    private RequestSpecification baseRequest() {
        return RestAssured.given().log().uri();
    }

    /**
     * Creates a request specification for JSON payloads.
     *
     * @param body The object to be serialized as JSON
     */
    private RequestSpecification jsonRequest(Object body) {
        return baseRequest()
                .contentType("application/json")
                .body(body);
    }

    /**
     * Fetches a player by name and NFL team.
     */
    public Response getPlayer(String name, String nflTeam) {
        return baseRequest()
                .queryParam("name", name)
                .when()
                .get(PLAYER_BASE + "/team/" + nflTeam);
    }

    /**
     * Fetches a player by their database ID.
     */
    public Response getPlayerById(String id) {
        return baseRequest()
                .when()
                .get(PLAYER_BASE + "/" + id);
    }

    /**
     * Fetches the Scare Factor analysis for a specific player.
     */
    public Response getScareFactor(String id) {
        return baseRequest()
                .when()
                .get(PLAYER_BASE + "/" + id + "/analysis");
    }

    /**
     * Fetches all players with optional filtering and pagination.
     */
    public Response getAllPlayers(String position, Integer page, Integer size) {
        Map<String, Object> params = new HashMap<>();
        if (position != null) params.put("position", position);
        if (page != null) params.put("page", page);
        if (size != null) params.put("size", size);

        return baseRequest()
                .queryParams(params)
                .when()
                .get(PLAYER_BASE);
    }

    /**
     * Creates a new fantasy team.
     */
    public Response createTeam(String name, List<PlayerRequest> players) {
        TeamRequest request = new TeamRequest();
        request.setName(name);
        request.setRoster(players);

        return jsonRequest(request)
                .when()
                .post(TEAM_BASE + "/create");
    }

    /**
     * Fetches a team by its ID.
     */
    public Response getTeamById(String id) {
        return baseRequest()
                .when()
                .get(TEAM_BASE + "/" + id);
    }

    /**
     * Fetches all fantasy teams.
     */
    public Response getAlLTeams() {
        return baseRequest()
                .when()
                .get(TEAM_BASE);
    }

    /**
     * Updates an existing fantasy team.
     */
    public Response updateTeam(String id, String name, List<PlayerRequest> players) {
        TeamRequest request = new TeamRequest();
        request.setName(name);
        request.setRoster(players);

        return jsonRequest(request)
                .when()
                .put(TEAM_BASE + "/" + id);
    }

    /**
     * Deletes a fantasy team by its ID.
     */
    public Response deleteTeam(String id) {
        return baseRequest()
                .when()
                .delete(TEAM_BASE + "/" + id);
    }

    /**
     * Initiates a matchup analysis between two players.
     */
    public Response createPlayerMatchup(String id1, String id2) {
        PlayerMatchupRequest request = new PlayerMatchupRequest();
        request.setPlayer1Id(Long.parseLong(id1));
        request.setPlayer2Id(Long.parseLong(id2));

        return jsonRequest(request)
                .when()
                .post(MATCHUP_BASE + "/player/create");
    }

    /**
     * Fetches a player matchup report by ID.
     */
    public Response getPlayerMatchupById(String id) {
        return baseRequest()
                .when()
                .get(MATCHUP_BASE + "/player/" + id);
    }

    /**
     * Initiates a matchup analysis between two fantasy teams.
     */
    public Response createTeamMatchup(String id1, String id2) {
        TeamMatchupRequest request = new TeamMatchupRequest();
        request.setTeam1Id(Long.parseLong(id1));
        request.setTeam2Id(Long.parseLong(id2));

        return jsonRequest(request)
                .when()
                .post(MATCHUP_BASE + "/team/create");
    }

    /**
     * Fetches a team matchup report by ID.
     */
    public Response getTeamMatchupById(String id) {
        return baseRequest()
                .when()
                .get(MATCHUP_BASE + "/team/" + id);
    }
}
