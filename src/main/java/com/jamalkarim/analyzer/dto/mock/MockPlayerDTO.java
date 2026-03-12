package com.jamalkarim.analyzer.dto.mock;

import lombok.Data;

@Data
public class MockPlayerDTO {
    private String name;
    private String nflTeam;
    private String position;
    private MockStatsDTO stats;
}
