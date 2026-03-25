package com.jamalkarim.analyzer.cucumber.utils;

import com.jamalkarim.analyzer.dto.requests.PlayerRequest;
import com.jamalkarim.analyzer.dto.requests.TeamRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiClient {

    public Response getPlayer(String name, String nflTeam) {
        return RestAssured.given()
                .log().uri()
                .queryParam("name", name)
                .when()
                .get("/api/player/team/" + nflTeam);
    }

    public Response getPlayerById(String id) {
        return RestAssured.given()
                .log().uri()
                .when()
                .get("/api/player/" + id);
    }

    public Response getScareFactor(String id) {
        return RestAssured.given()
                .log().uri()
                .when()
                .get("/api/player/" + id + "/analysis");
    }

    public Response getAllPlayers() {
        return RestAssured.given()
                .log().uri()
                .when()
                .get("/api/player");
    }

    public Response getAllPlayers(int page, int size) {
        return RestAssured.given()
                .log().uri()
                .queryParam("page", page)
                .queryParam("size", size)
                .when()
                .get("/api/player");
    }

    public Response getAllPlayersByPosition(String position) {
        return RestAssured.given()
                .log().uri()
                .queryParam("position", position)
                .when()
                .get("/api/player");
    }

    public Response createTeam(String name, List<PlayerRequest> players) {
        TeamRequest request = new TeamRequest();
        request.setName(name);
        request.setRoster(players);

        return RestAssured.given()
                .contentType("application/json")
                .body(request)
                .log().uri()
                .when()
                .post("/api/team/create");
    }

    public Response getTeamById(String id) {
        return RestAssured.given()
                .log().uri()
                .when()
                .get("/api/team/" + id);
    }

    public Response getAlLTeams() {
        return RestAssured.given()
                .log().uri()
                .when()
                .get("/api/team");
    }

    public Response updateTeam(String id, String name, List<PlayerRequest> players) {
        TeamRequest request = new TeamRequest();
        request.setName(name);
        request.setRoster(players);

        return RestAssured.given()
                .contentType("application/json")
                .body(request)
                .log().uri()
                .when()
                .put("/api/team/" + id);
    }
}
