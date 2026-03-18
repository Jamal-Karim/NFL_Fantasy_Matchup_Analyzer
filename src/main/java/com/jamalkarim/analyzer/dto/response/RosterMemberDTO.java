package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jamalkarim.analyzer.domain.enums.Position;
import lombok.Data;

@Data
@JsonPropertyOrder({"id", "position", "name", "nflTeam"})
public class RosterMemberDTO {
    private long id;
    private String name;
    private String nflTeam;
    private Position position;
}
