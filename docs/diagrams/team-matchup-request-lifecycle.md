sequenceDiagram
autonumber
participant Client
participant Controller
participant MatchupService
participant TeamService
participant Analyzer
participant Repository

    Client->>Controller: POST /api/matchup/team/create

    Controller->>TeamService: fetch teams
    TeamService-->>Controller: Team data

    Controller->>MatchupService: createMatchup(team1, team2)

    MatchupService->>Analyzer: analyzeMatchup(team1, team2)

    loop For each player
        Analyzer->>Analyzer: compute player impact
    end

    Analyzer-->>MatchupService: Matchup result

    MatchupService->>Repository: save result
    Repository-->>MatchupService: saved entity

    MatchupService-->>Controller: response DTO
    Controller-->>Client: 200 OK