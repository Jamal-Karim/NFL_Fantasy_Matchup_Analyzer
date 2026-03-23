package com.jamalkarim.analyzer.dto.real;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatsExternalDTO {

    private StatisticsContainer statistics;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StatisticsContainer {
        private SplitsContainer splits;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SplitsContainer {
        // determined from what we pass into the endpoint
        private Integer season;

        // This is where your actual categories list lives
        private List<CategoryDTO> categories;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CategoryDTO {
        private String name;
        @com.fasterxml.jackson.annotation.JsonProperty("stats")
        private List<InternalStatsDTO> statsInCategory;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InternalStatsDTO {
        private String name;
        private String value;
    }

}
