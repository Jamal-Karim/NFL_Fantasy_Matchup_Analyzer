package com.jamalkarim.analyzer.cucumber.steps;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Configuration class to integrate Cucumber with the Spring Boot application context.
 * This class ensures that Spring components, services, and the database are available
 * during behavioral test execution.
 */
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("automation")
public class CucumberSpringConfiguration {
}
