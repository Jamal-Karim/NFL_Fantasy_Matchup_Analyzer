package com.jamalkarim.analyzer.cucumber.steps;

import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

public class ApiVerify {

    private final TestContext testContext;

    public ApiVerify(TestContext testContext) {
        this.testContext = testContext;
    }

    @Then("^the api call should be successful$")
    public void verifyApiSuccess() {
        Assertions.assertEquals(200, testContext.getResponse().getStatusCode(), "API Response was not successful");
    }
}
