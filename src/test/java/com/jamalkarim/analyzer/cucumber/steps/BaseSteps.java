package com.jamalkarim.analyzer.cucumber.steps;

import com.jamalkarim.analyzer.cucumber.utils.ApiClient;
import com.jamalkarim.analyzer.cucumber.utils.DbUtils;
import com.jamalkarim.analyzer.cucumber.utils.TestVariables;

/**
 * Base class for all Cucumber step definition classes.
 * Provides access to shared utilities and state management.
 */
public abstract class BaseSteps {
    protected final ApiClient client;
    protected final TestContext testContext;
    protected final TestVariables testVariables;
    protected final DbUtils dbUtils;

    public BaseSteps(ApiClient client, TestContext testContext, TestVariables testVariables, DbUtils dbUtils) {
        this.client = client;
        this.testContext = testContext;
        this.testVariables = testVariables;
        this.dbUtils = dbUtils;
    }
}
