package com.jamalkarim.analyzer.cucumber.hooks;

import com.jamalkarim.analyzer.cucumber.steps.TestContext;
import com.jamalkarim.analyzer.cucumber.utils.DbUtils;
import com.jamalkarim.analyzer.cucumber.utils.TestVariables;
import io.cucumber.java.After;
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

    /**
     * Constructs GlobalHooks with required infrastructure components.
     *
     * @param dbUtils       Utility for database operations
     * @param testVariables Utility for managing scenario-scoped variables
     * @param testContext   Shared state container for the current scenario
     */
    public GlobalHooks(DbUtils dbUtils, TestVariables testVariables, TestContext testContext) {
        this.dbUtils = dbUtils;
        this.testVariables = testVariables;
        this.testContext = testContext;
    }

    /**
     * Initializes the test environment before each scenario.
     * Sets the API port, clears the database, and resets shared state.
     */
    @Before
    public void setup() {
        RestAssured.port = port;
        dbUtils.clearDatabase();
        testVariables.clearAll();
        testContext.clear();
    }

    /**
     * Cleans up the database after each scenario.
     */
    @After
    public void cleanUp() {
        dbUtils.clearDatabase();
    }
}
