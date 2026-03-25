package com.jamalkarim.analyzer.cucumber.steps;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class ApiVerify {

    private final TestContext testContext;

    public ApiVerify(TestContext testContext) {
        this.testContext = testContext;
    }

    @Then("^the api call should be successful$")
    public void verifyApiSuccess() {
        Assertions.assertEquals(200, testContext.getResponse().getStatusCode(), "API Response was not successful");
    }

    @Then("^the response body has:$")
    public void verifyResponseBody(List<List<String>> table) {
        for (List<String> row : table) {
            String key = row.get(0);
            String value = row.get(1);
            compareResponses(testContext.getResponse(), key, value);
        }
    }

    private void compareResponses(Response response, String key, String value) {
        if (key.equals("status_code")) {
            Assertions.assertEquals(Integer.parseInt(value), response.getStatusCode(),
                    "HTTP Status Code mismatch");
        } else {
            String jsonKey = testContext.getResponse().jsonPath().getString(key);
            Assertions.assertEquals(value, jsonKey, String.format("JSON field [%s] mismatch", key));
        }
    }
}
