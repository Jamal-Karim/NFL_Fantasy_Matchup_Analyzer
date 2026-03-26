package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.cucumber.utils.ApiClient;
import com.jamalkarim.analyzer.cucumber.utils.DbUtils;
import com.jamalkarim.analyzer.cucumber.utils.TestVariables;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.List;

/**
 * Generic step definitions that can be used across multiple features.
 * Handles common API verifications and state management.
 */
public class CommonSteps extends BaseSteps {

    public CommonSteps(ApiClient client, TestContext testContext, TestVariables testVariables, DbUtils dbUtils) {
        super(client, testContext, testVariables, dbUtils);
    }

    /**
     * Verifies that the last API call returned a 200 OK status code.
     */
    @Then("^the api call should be successful$")
    public void verifyApiSuccess() {
        Assertions.assertEquals(200, testContext.getResponse().getStatusCode(), "API Response was not successful");
    }

    /**
     * Verifies multiple fields in the response body against expected values.
     *
     * @param table A data table containing key-value pairs of JSON paths and expected values
     */
    @Then("^the response body has:$")
    public void verifyResponseBody(List<List<String>> table) {
        for (List<String> row : table) {
            String key = row.get(0);
            String value = row.get(1);
            compareResponses(testContext.getResponse(), key, value);
        }
    }

    /**
     * Helper method to compare a single JSON field or status code.
     */
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
