package com.jamalkarim.analyzer.dto.real;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object for mapping player statistics from the external NFL API.
 * This class follows the hierarchical structure of the ESPN API response.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatsExternalDTO {

    private StatisticsContainer statistics;

    /**
     * Container for player statistics categories.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StatisticsContainer {
        private SplitsContainer splits;
    }

    /**
     * Container for seasonal statistics splits.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SplitsContainer {
        // determined from what we pass into the endpoint
        private Integer season;

        // This is where your actual categories list lives
        private List<CategoryDTO> categories;
    }

    /**
     * DTO for a specific category of statistics (e.g., "passing", "rushing").
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CategoryDTO {
        private String name;
        @com.fasterxml.jackson.annotation.JsonProperty("stats")
        private List<InternalStatsDTO> statsInCategory;
    }

    /**
     * DTO for an individual statistic within a category.
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InternalStatsDTO {
        private String name;
        private String value;
    }

}
