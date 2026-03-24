package com.jamalkarim.analyzer.dto.real;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<InjuryDTO> injuries;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private StatsExternalDTO lastSeasonStats;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private StatsExternalDTO currentSeasonStats;


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

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RosterGroup {
        private String position; // This will hold "offense", "defense", etc.
        private List<AthleteExternalDTO> items; // This is where the actual players are!
    }

    /**
     * Wrapper for the "Team Roster" endpoint.
     * JSON looks like: { "athletes": [ {...}, {...} ] }
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamRosterWrapper {
        private List<RosterGroup> athletes;
    }

    /**
     * Wrapper for the "Single Player Detail" endpoint.
     * JSON looks like: { "athlete": {...} }
     */
    @Data
    public static class PlayerDetailWrapper {
        private AthleteExternalDTO athlete;
    }

    public int getDraftPosition(String draftInfo) {
        Pattern pattern = Pattern.compile("Pk (\\d+)");
        Matcher matcher = pattern.matcher(draftInfo);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    public boolean isRookie(String draftInfo) {
        Pattern pattern = Pattern.compile("^(\\d{4})");
        Matcher matcher = pattern.matcher(draftInfo);

        if (matcher.find()) {
            int draftYear = Integer.parseInt(matcher.group(1));
            int year = LocalDate.now().getYear();

            return draftYear == year;
        }

        return false;
    }

    public boolean hasActiveInjuries() {
        return injuries != null && !injuries.isEmpty();
    }
}
