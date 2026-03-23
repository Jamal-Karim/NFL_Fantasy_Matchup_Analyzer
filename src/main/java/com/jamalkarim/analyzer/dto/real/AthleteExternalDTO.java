package com.jamalkarim.analyzer.dto.real;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AthleteExternalDTO {
    private String id;

    @JsonProperty("fullName")
    private String name;

    private PositionDTO position;

    @JsonProperty("team")
    private TeamDTO nflTeam;

    // contains stats for year drafted and draft position
    // uses info here to determines isRookie stat to put into our domain player logic
    @JsonProperty("displayDraft")
    private String draftInfo;

    // if null set isInjured in domain player to false
    private List<InjuryDTO> injuries;


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PositionDTO {
        private String abbreviation;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InjuryDTO {
        private String status;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamDTO {
        private String abbreviation;
        private String displayName;
    }

    /**
     * Wrapper for the "Team Roster" endpoint.
     * JSON looks like: { "athletes": [ {...}, {...} ] }
     */
    @Data
    public static class TeamRosterWrapper {
        private List<AthleteExternalDTO> athletes;
    }

    /**
     * Wrapper for the "Single Player Detail" endpoint.
     * JSON looks like: { "athlete": {...} }
     */
    @Data
    public static class PlayerDetailWrapper {
        private AthleteExternalDTO athlete;
    }
}
