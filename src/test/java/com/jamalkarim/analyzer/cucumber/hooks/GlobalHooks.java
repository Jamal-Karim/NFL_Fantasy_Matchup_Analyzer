package com.jamalkarim.analyzer.cucumber.hooks;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import org.springframework.boot.test.web.server.LocalServerPort;

/**
 * Global hooks for Cucumber scenarios.
 * Handles environment setup and teardown that applies to all features.
 */
public class GlobalHooks {

    @LocalServerPort
    private int port;

    /**
     * Sets the global RestAssured port before each scenario starts.
     * This ensures the ApiClient knows where to send requests without
     * having to pass the port manually.
     */
    @Before
    public void setup() {
        RestAssured.port = port;
    }
}
