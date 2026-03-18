package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({"id", "name", "roster"})
public class TeamResponseDTO {
    private long id;
    private String name;
    private List<RosterMemberDTO> roster;
}
