package com.jamalkarim.analyzer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Configuration class for OpenAPI (Swagger) documentation.
 * This class defines the metadata, global tags, and visual organization of the API documentation.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures the primary OpenAPI bean with professional metadata and organized documentation tags.
     *
     * @return The configured {@link OpenAPI} specification.
     */
    @Bean
    public OpenAPI nflFantasyAnalyzerOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("NFL Fantasy Football Analyzer API")
                        .description("### Professional Fantasy Football Analytical Engine\n\n" +
                                "This API provides advanced analytical tools for NFL Fantasy Football enthusiasts and developers. " +
                                "It leverages high-performance data processing to calculate 'Scare Factors'—a proprietary metric " +
                                "evaluating player performance potential.\n\n" +
                                "Key Features:\n" +
                                "* **Real-time Data Sync**: Seamlessly integrates with external NFL providers.\n" +
                                "* **Predictive Analysis**: Advanced matchup evaluation for players and full rosters.\n" +
                                "* **Robust Architecture**: Built with Spring Boot 3, JPA/Hibernate, and MySQL.\n" +
                                "* **Quality Assured**: 100% feature coverage with Cucumber BDD testing.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Jamal Karim")
                                .url("https://github.com/Jamal-Karim")
                                .email("1jamalkarim@gmail.com")))
                .tags(Arrays.asList(
                        new Tag().name("1. Players").description("Management and analytical insights for NFL Players"),
                        new Tag().name("2. Teams").description("Roster management and fantasy team configuration"),
                        new Tag().name("3. Matchups").description("Head-to-head comparison and predictive performance metrics")
                ));
    }

    /**
     * Provides a customizer to sort API endpoints in the Swagger UI.
     * Endpoints are sorted based on the custom 'x-order' extension property found in operation metadata.
     *
     * @return An {@link OpenApiCustomizer} that reorders paths alphabetically/numerically by 'x-order'.
     */
    @Bean
    public OpenApiCustomizer sortEndpointsCustomiser() {
        return openApi -> {
            Paths paths = openApi.getPaths();
            if (paths != null) {
                Paths sortedPaths = new Paths();
                paths.entrySet().stream()
                        .sorted(Comparator.comparing(entry -> {
                            var operation = entry.getValue().readOperations().get(0);
                            if (operation.getExtensions() != null && operation.getExtensions().containsKey("x-order")) {
                                return operation.getExtensions().get("x-order").toString();
                            }
                            return "99";
                        }))
                        .forEach(entry -> sortedPaths.addPathItem(entry.getKey(), entry.getValue()));
                openApi.setPaths(sortedPaths);
            }
        };
    }
}