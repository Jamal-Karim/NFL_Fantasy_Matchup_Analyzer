package com.jamalkarim.analyzer.domain.enums;

/**
 * Enum representing all 32 NFL teams with their external API IDs,
 * abbreviations, and display names.
 */
public enum NflTeams {
    ARIZONA_CARDINALS("22", "ARI", "Arizona Cardinals"),
    ATLANTA_FALCONS("1", "ATL", "Atlanta Falcons"),
    BALTIMORE_RAVENS("33", "BAL", "Baltimore Ravens"),
    BUFFALO_BILLS("2", "BUF", "Buffalo Bills"),
    CAROLINA_PANTHERS("29", "CAR", "Carolina Panthers"),
    CHICAGO_BEARS("3", "CHI", "Chicago Bears"),
    CINCINNATI_BENGALS("4", "CIN", "Cincinnati Bengals"),
    CLEVELAND_BROWNS("5", "CLE", "Cleveland Browns"),
    DALLAS_COWBOYS("6", "DAL", "Dallas Cowboys"),
    DENVER_BRONCOS("7", "DEN", "Denver Broncos"),
    DETROIT_LIONS("8", "DET", "Detroit Lions"),
    GREEN_BAY_PACKERS("9", "GB", "Green Bay Packers"),
    HOUSTON_TEXANS("34", "HOU", "Houston Texans"),
    INDIANAPOLIS_COLTS("11", "IND", "Indianapolis Colts"),
    JACKSONVILLE_JAGUARS("30", "JAX", "Jacksonville Jaguars"),
    KANSAS_CITY_CHIEFS("12", "KC", "Kansas City Chiefs"),
    LAS_VEGAS_RAIDERS("13", "LV", "Las Vegas Raiders"),
    LOS_ANGELES_CHARGERS("24", "LAC", "Los Angeles Chargers"),
    LOS_ANGELES_RAMS("14", "LAR", "Los Angeles Rams"),
    MIAMI_DOLPHINS("15", "MIA", "Miami Dolphins"),
    MINNESOTA_VIKINGS("16", "MIN", "Minnesota Vikings"),
    NEW_ENGLAND_PATRIOTS("17", "NE", "New England Patriots"),
    NEW_ORLEANS_SAINTS("18", "NO", "New Orleans Saints"),
    NEW_YORK_GIANTS("19", "NYG", "New York Giants"),
    NEW_YORK_JETS("20", "NYJ", "New York Jets"),
    PHILADELPHIA_EAGLES("21", "PHI", "Philadelphia Eagles"),
    PITTSBURGH_STEELERS("23", "PIT", "Pittsburgh Steelers"),
    SAN_FRANCISCO_49ERS("25", "SF", "San Francisco 49ers"),
    SEATTLE_SEAHAWKS("26", "SEA", "Seattle Seahawks"),
    TAMPA_BAY_BUCCANEERS("27", "TB", "Tampa Bay Buccaneers"),
    TENNESSEE_TITANS("10", "TEN", "Tennessee Titans"),
    WASHINGTON_COMMANDERS("28", "WSH", "Washington Commanders");

    private final String externalId;
    private final String abbreviation;
    private final String displayName;

    NflTeams(String externalId, String abbreviation, String displayName) {
        this.externalId = externalId;
        this.abbreviation = abbreviation;
        this.displayName = displayName;
    }

    /**
     * Finds the external API ID based on a team abbreviation.
     */
    public static String getIdByAbbreviation(String abbr) {
        for (NflTeams team : values()) {
            if (team.abbreviation.equalsIgnoreCase(abbr)) {
                return team.externalId;
            }
        }
        throw new IllegalArgumentException("Unknown NFL Team abbreviation: " + abbr);
    }
}
