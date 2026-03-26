package com.jamalkarim.analyzer.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

/**
 * Entry point for running Cucumber tests using the JUnit Platform.
 * Configures the glue code location and the path to the feature files.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.jamalkarim.analyzer.cucumber")
public class CucumberTestRunner {
}
