package com.jamalkarim.analyzer.dto.real;

import lombok.Data;

import java.util.List;

@Data
public class StatsExternalDTO {

    // determined from what we pass into the endpoint
    private Integer season;

    private List<CategoryDTO> categories;

    @Data
    public static class CategoryDTO {
        private String name;
        private List<InternalStatsDTO> statsInCategory;
    }

    @Data
    public static class InternalStatsDTO {
        private String name;
        private String value;
    }

}
