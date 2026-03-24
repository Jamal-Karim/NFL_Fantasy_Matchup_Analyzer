package com.jamalkarim.analyzer.steps;

import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.web.server.LocalServerPort;

public class PlayerSteps {
    @LocalServerPort
    private int port;

    private final TestContext testContext;

    public PlayerSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Given("^I fetch the player ([a-zA-Z\\s]+) on team ([A-Z]{2,3})$")
    public void fetchPlayer(String name, String nflTeam) {
        Response response = RestAssured.given()
                .port(port)
                .queryParam("name", name)
                .log().uri()
                .when()
                .get("/api/player/team/" + nflTeam);

        response.prettyPrint();
        testContext.setResponse(response);
    }
}
