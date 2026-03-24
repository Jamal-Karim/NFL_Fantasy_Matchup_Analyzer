package com.jamalkarim.analyzer.cucumber.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.Data;

@Data
public class ApiClient {

    private int port;

    public ApiClient(int port) {
        this.port = port;
    }

    public Response getPlayer(String name, String nflTeam) {
        return RestAssured.given()
                .port(port)
                .log().uri()
                .queryParam("name", name)
                .when()
                .get("/api/player/team/" + nflTeam);
    }

}
