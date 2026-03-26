@EndToEnd
Feature: Full End to End Flow of a User

  Background: Create a full fantasy team
    Given I create a fantasy team Fantasy Team 1:
      | name             | team |
      | Patrick Mahomes  | KC   |
      | Saquon Barkley   | PHI  |
      | James Cook       | BUF  |
      | Justin Jefferson | MIN  |
      | Jaylen Waddle    | MIA  |
      | Brock Bowers     | LV   |
      | Travis Kelce     | KC   |
    Then the api call should be successful
    Then the team should be saved to the database
    And the team id is saved to {team1}

    Given I create a fantasy team Fantasy Team 2:
      | name           | team |
      | Josh Allen     | BUF  |
      | Ashton Jeanty  | LV   |
      | Bijan Robinson | ATL  |
      | AJ Brown       | PHI  |
      | Puka Nacua     | LAR  |
      | Tucker Kraft   | GB   |
      | Jamarr Chase   | CIN  |
    Then the api call should be successful
    Then the team should be saved to the database
    And the team id is saved to {team2}

  Scenario: Run team matchup between 2 teams
    When I run the team matchup for {team1} and {team2}
    Then the api call should be successful
    Then the response body has:
      | data.team_1_win_probability | 1.88     |
      | data.team_2_win_probability | 98.12    |
      | data.advantage              | DOMINANT |

  Scenario: Fetching 2 other players and running a matchup
    Given I fetch the players:
      | name          | nfl_team | variable |
      | CeeDee Lamb   | DAL      | {id1}    |
      | Jaylen Waddle | MIA      | {id2}    |
    Then the api call should be successful
    When I run the player matchup for {id1} and {id2}
    Then the response body has:
      | data.winner    | CeeDee Lamb   |
      | data.loser     | Jaylen Waddle |
      | data.advantage | SLIGHT_EDGE   |

  Scenario: Update team to have better player
    When I update the team {team1}:
      | name             | team |
      | Patrick Mahomes  | KC   |
      | Saquon Barkley   | PHI  |
      | James Cook       | BUF  |
      | Justin Jefferson | MIN  |
      | CeeDee Lamb      | DAL  |
      | Brock Bowers     | LV   |
      | Travis Kelce     | KC   |
    Then the api call should be successful
    Then Jaylen Waddle should not be on team {team1}
    When I run the team matchup for {team1} and {team2}
    Then the api call should be successful
    Then the response body has:
      | data.team_1_win_probability | 2.52     |
      | data.team_2_win_probability | 97.48    |
      | data.advantage              | DOMINANT |