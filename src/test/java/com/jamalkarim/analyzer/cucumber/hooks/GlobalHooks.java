package com.jamalkarim.analyzer.cucumber.hooks;

import com.jamalkarim.analyzer.cucumber.steps.TestContext;
import com.jamalkarim.analyzer.cucumber.utils.DbUtils;
import com.jamalkarim.analyzer.cucumber.utils.TestVariables;
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

    private final DbUtils dbUtils;
    private final TestVariables testVariables;
    private final TestContext testContext;

    public GlobalHooks(DbUtils dbUtils, TestVariables testVariables, TestContext testContext) {
        this.dbUtils = dbUtils;
        this.testVariables = testVariables;
        this.testContext = testContext;
    }

    @Before
    public void setup() {
        RestAssured.port = port;
        dbUtils.clearDatabase();
        testVariables.clearAll();
        testContext.clear();
    }
}
