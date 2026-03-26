@Matchup
Feature: Matchup feature

  Scenario: Create a player matchup
    Given I fetch the players:
      | name            | nfl_team | variable |
      | Patrick Mahomes | KC       | {id1}    |
      | Brock Bowers    | LV       | {id2}    |
    Then the api call should be successful
    When I run the player matchup for {id1} and {id2}
    Then the response body has:
      | data.winner    | Brock Bowers    |
      | data.loser     | Patrick Mahomes |
      | data.advantage | DOMINANT        |
    And I save the player matchup id to {matchupId1}
    Then the player matchup should be saved to the database
    When I request the player matchup with id {matchupId1}
    Then the response body has:
      | data.winner    | Brock Bowers    |
      | data.loser     | Patrick Mahomes |
      | data.advantage | DOMINANT        |
    When I request the player matchup with id 4266
    Then the response body has:
      | status_code | 404                                        |
      | status      | ERROR                                      |
      | message     | Player matchup with id 4266 does not exist |

  Scenario: Failure to create player matchup between the same player
    Given I fetch the players:
      | name            | nfl_team | variable |
      | Patrick Mahomes | KC       | {id1}    |
      | Brock Bowers    | LV       | {id2}    |
    Then the api call should be successful
    When I run the player matchup for {id1} and {id1}
    Then the response body has:
      | status_code | 400                                           |
      | status      | ERROR                                         |
      | message     | Cannot create matchup between the same player |

  Scenario: Create a team matchup
    Given I create a fantasy team team1:
      | name            | team |
      | Patrick Mahomes | KC   |
      | Saquon Barkley  | PHI  |
      | Brock Bowers    | LV   |
    Then the api call should be successful
    And the team id is saved to {team1}
    Given I create a fantasy team team2:
      | name         | team |
      | Josh Allen   | BUF  |
      | James Cook   | BUF  |
      | Travis Kelce | KC   |
    Then the api call should be successful
    And the team id is saved to {team2}
    When I run the team matchup for {team1} and {team2}
    Then the api call should be successful
    Then the response body has:
      | data.team_1_win_probability | 14.96      |
      | data.team_2_win_probability | 85.04      |
      | data.advantage              | CLEAR_EDGE |
    And I save the team matchup id to {matchupId2}
    Then the amount of player matchups saved to the database is 3
    When I request the team matchup with id {matchupId2}
    Then the response body has:
      | data.team_1_win_probability | 14.96      |
      | data.team_2_win_probability | 85.04      |
      | data.advantage              | CLEAR_EDGE |
    When I request the team matchup with id 4266
    Then the response body has:
      | status_code | 404                                      |
      | status      | ERROR                                    |
      | message     | Team matchup with id 4266 does not exist |

  Scenario: Failure to create player matchup between the same player
    Given I create a fantasy team team1:
      | name            | team |
      | Patrick Mahomes | KC   |
      | Saquon Barkley  | PHI  |
      | Brock Bowers    | LV   |
    Then the api call should be successful
    And the team id is saved to {team4}
    When I run the team matchup for {team4} and {team4}
    Then the response body has:
      | status_code | 400                                         |
      | status      | ERROR                                       |
      | message     | Cannot create matchup between the same team |