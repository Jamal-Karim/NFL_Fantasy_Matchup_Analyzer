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

    public Response getAllPlayers() {
        return RestAssured.given()
                .log().uri()
                .when()
                .get("/api/player");
    }
}
