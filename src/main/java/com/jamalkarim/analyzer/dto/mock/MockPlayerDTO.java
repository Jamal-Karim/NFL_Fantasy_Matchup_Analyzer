package com.jamalkarim.analyzer.dto.mock;

import com.jamalkarim.analyzer.domain.enums.Position;
import lombok.Data;

@Data
public class MockPlayerDTO {
    private String name;
    private String nflTeam;
    private Position position;
    private MockStatsDTO stats;
}
