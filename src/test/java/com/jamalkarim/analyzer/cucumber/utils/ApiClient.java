package com.jamalkarim.analyzer.cucumber.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

@Component
public class ApiClient {

    public Response getPlayer(String name, String nflTeam) {
        return RestAssured.given()
                .log().uri()
                .queryParam("name", name)
                .when()
                .get("/api/player/team/" + nflTeam);
    }

    public Response getScareFactor(long id) {
        return RestAssured.given()
                .log().uri()
                .when()
                .get("/api/player/" + id + "/analysis");
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
}
