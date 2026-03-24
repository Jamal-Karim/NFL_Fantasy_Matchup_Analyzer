package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.cucumber.utils.ApiClient;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import org.springframework.boot.test.web.server.LocalServerPort;

import jakarta.annotation.PostConstruct;

public class PlayerSteps {

    @LocalServerPort
    private int port;

    private ApiClient client;
    private final TestContext testContext;

    public PlayerSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @PostConstruct
    public void setup() {
        this.client = new ApiClient(port);
    }

    @Given("^I fetch the player ([a-zA-Z\\s]+) on team ([A-Z]{2,3})$")
    public void fetchPlayer(String name, String nflTeam) {
        Response response = client.getPlayer(name, nflTeam);
        response.prettyPrint();
        testContext.setResponse(response);
    }
}
