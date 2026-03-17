package com.jamalkarim.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the NFL Fantasy Football Analyzer application.
 * This Spring Boot application provides tools to analyze player performance
 * and predict matchup outcomes based on various statistical factors.
 */
@SpringBootApplication
public class NflFantasyFootballAnalyzerApplication {

	/**
	 * Main method to launch the application.
	 *
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(NflFantasyFootballAnalyzerApplication.class, args);
	}

}
