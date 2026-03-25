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

@Component
public class ApiClient {

    private static final String PLAYER_BASE = "/api/player";
    private static final String TEAM_BASE = "/api/team";
    private static final String MATCHUP_BASE = "/api/matchup";

    private RequestSpecification baseRequest() {
        return RestAssured.given().log().uri();
    }

    private RequestSpecification jsonRequest(Object body) {
        return baseRequest()
                .contentType("application/json")
                .body(body);
    }

    public Response getPlayer(String name, String nflTeam) {
        return baseRequest()
                .queryParam("name", name)
                .when()
                .get(PLAYER_BASE + "/team/" + nflTeam);
    }

    public Response getPlayerById(String id) {
        return baseRequest()
                .when()
                .get(PLAYER_BASE + "/" + id);
    }

    public Response getScareFactor(String id) {
        return baseRequest()
                .when()
                .get(PLAYER_BASE + "/" + id + "/analysis");
    }

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

    public Response createTeam(String name, List<PlayerRequest> players) {
        TeamRequest request = new TeamRequest();
        request.setName(name);
        request.setRoster(players);

        return jsonRequest(request)
                .when()
                .post(TEAM_BASE + "/create");
    }

    public Response getTeamById(String id) {
        return baseRequest()
                .when()
                .get(TEAM_BASE + "/" + id);
    }

    public Response getAlLTeams() {
        return baseRequest()
                .when()
                .get(TEAM_BASE);
    }

    public Response updateTeam(String id, String name, List<PlayerRequest> players) {
        TeamRequest request = new TeamRequest();
        request.setName(name);
        request.setRoster(players);

        return jsonRequest(request)
                .when()
                .put(TEAM_BASE + "/" + id);
    }

    public Response deleteTeam(String id) {
        return baseRequest()
                .when()
                .delete(TEAM_BASE + "/" + id);
    }

    public Response createPlayerMatchup(String id1, String id2) {
        PlayerMatchupRequest request = new PlayerMatchupRequest();
        request.setPlayer1Id(Long.parseLong(id1));
        request.setPlayer2Id(Long.parseLong(id2));

        return jsonRequest(request)
                .when()
                .post(MATCHUP_BASE + "/player/create");
    }

    public Response getPlayerMatchupById(String id) {
        return baseRequest()
                .when()
                .get(MATCHUP_BASE + "/player/" + id);
    }

    public Response createTeamMatchup(String id1, String id2) {
        TeamMatchupRequest request = new TeamMatchupRequest();
        request.setTeam1Id(Long.parseLong(id1));
        request.setTeam2Id(Long.parseLong(id2));

        return jsonRequest(request)
                .when()
                .post(MATCHUP_BASE + "/team/create");
    }

    public Response getTeamMatchupById(String id) {
        return baseRequest()
                .when()
                .get(MATCHUP_BASE + "/team/" + id);
    }
}
