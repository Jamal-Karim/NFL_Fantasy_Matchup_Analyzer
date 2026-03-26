@Team
Feature: Team management and roster construction

  Scenario: Create a full fantasy team
    Given I create a fantasy team Full Cucumber team:
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
    And I save the response id to {team1}
    When I request the team with id {team1}
    Then the response body has:
      | data.name          | Full Cucumber team |
      | data.roster.size() | 7                  |

  Scenario: Create a single fantasy team and verify player is saved to team
    Given I create a fantasy team Small Cucumber team:
      | name            | team |
      | Patrick Mahomes | KC   |
      | Saquon Barkley  | PHI  |
      | Brock Bowers    | LV   |
    Then the api call should be successful
    Then the team should be saved to the database
    And I save the response id to {team2}
    Then Patrick Mahomes should be on team {team2}
    When I request the team with id {team2}
    And the response body has:
      | data.name          | Small Cucumber team |
      | data.roster.size() | 3                   |

  Scenario: Update a fantasy team should be successful and remove and add players accordingly
    Given I create a fantasy team Cucumber team 3:
      | name            | team |
      | Patrick Mahomes | KC   |
      | Saquon Barkley  | PHI  |
      | Brock Bowers    | LV   |
    Then the api call should be successful
    And I save the response id to {team3}
    Then Patrick Mahomes should be on team {team3}
    When I update the team {team3} to Cucumber update:
      | name           | team |
      | Josh Allen     | BUF  |
      | Saquon Barkley | PHI  |
      | Brock Bowers   | LV   |
    Then the api call should be successful
    Then Patrick Mahomes should not be on team {team3}
    When I request the team with id {team3}
    And the response body has:
      | data.name          | Cucumber update |
      | data.roster.size() | 3               |

  Scenario: Delete a fantasy team should be successful and remove players from team
    Given I create a fantasy team Delete Cucumber team:
      | name            | team |
      | Patrick Mahomes | KC   |
      | Saquon Barkley  | PHI  |
      | Brock Bowers    | LV   |
    Then the api call should be successful
    And I save the response id to {team4}
    Then Patrick Mahomes should be on team {team4}
    Then Saquon Barkley should be on team {team4}
    Then Brock Bowers should be on team {team4}
    When I delete the team with id {team4}
    Then the response body has:
      | status_code | 200                  |
      | status      | SUCCESS              |
      | message     | Operation successful |
    Then Patrick Mahomes should not be on team {team4}
    Then Saquon Barkley should not be on team {team4}
    Then Brock Bowers should not be on team {team4}
    And the team Delete Cucumber team should not exist in the database

  Scenario: Cannot delete team with invalid id
    When I delete the team with id 4266
    Then the response body has:
      | status_code | 404                         |
      | status      | ERROR                       |
      | message     | Team with id 4266 not found |

  Scenario: Cannot get team with invalid id
    When I request the team with id 4266
    Then the response body has:
      | status_code | 404                         |
      | status      | ERROR                       |
      | message     | Team with id 4266 not found |

  Scenario: Create a fantasy team with too many qbs should fail
    Given I create a fantasy team Cucumber team2:
      | name            | team |
      | Patrick Mahomes | KC   |
      | Josh Allen      | BUF  |
      | Brock Bowers    | LV   |
    Then the response body has:
      | status_code | 400                                                      |
      | status      | ERROR                                                    |
      | message     | Max QBs reached. Quarterbacks cannot fill the Flex slot. |

  Scenario: Create a team with a full roster including Flex
    Given I create a fantasy team Full Roster:
      | name                | team |
      | Patrick Mahomes     | KC   |
      | Saquon Barkley      | PHI  |
      | Christian McCaffrey | SF   |
      | Justin Jefferson    | MIN  |
      | Jaylen Waddle       | MIA  |
      | Brock Bowers        | LV   |
      | Breece Hall         | NYJ  |
    Then the api call should be successful
    And the response body has:
      | data.name | Full Roster |

  Scenario: Create a fantasy team with too many RBs should fail
    Given I create a fantasy team RB Heavy:
      | name                | team |
      | Patrick Mahomes     | KC   |
      | Saquon Barkley      | PHI  |
      | Christian McCaffrey | SF   |
      | Breece Hall         | NYJ  |
      | Bijan Robinson      | ATL  |
    Then the response body has:
      | status_code | 400                                                   |
      | status      | ERROR                                                 |
      | message     | Roster full: Both the RB and Flex slots are occupied. |

  Scenario: Failure to add player that already exists on a team
    Given I create a fantasy team fantasy team 1:
      | name            | team |
      | Patrick Mahomes | KC   |
      | Saquon Barkley  | PHI  |
      | Brock Bowers    | LV   |
    Then the api call should be successful
    Then the team should be saved to the database
    And I save the response id to {team5}
    Given I create a fantasy team fantasy team 2:
      | name         | team |
      | Josh Allen   | BUF  |
      | James Cook   | BUF  |
      | Brock Bowers | LV   |
    Then the response body has:
      | status_code | 409                                             |
      | status      | ERROR                                           |
      | message     | Brock Bowers is already on team: fantasy team 1 |

  Scenario: Get all teams
    Given I create a fantasy team team1:
      | name            | team |
      | Patrick Mahomes | KC   |
      | Saquon Barkley  | PHI  |
      | Brock Bowers    | LV   |
    Then the api call should be successful
    Given I create a fantasy team team2:
      | name         | team |
      | Josh Allen   | BUF  |
      | James Cook   | BUF  |
      | Travis Kelce | KC   |
    Then the api call should be successful
    Given I create a fantasy team team3:
      | name           | team |
      | Joe Burrow     | CIN  |
      | Bijan Robinson | ATL  |
      | Chris Olave    | NO   |
    Then the api call should be successful
    When I get all teams
    Then the response body has:
      | status             | SUCCESS |
      | data.totalElements | 3       |

  Scenario: Cannot create team with same name
    Given I create a fantasy team team1:
      | name            | team |
      | Patrick Mahomes | KC   |
      | Saquon Barkley  | PHI  |
      | Brock Bowers    | LV   |
    Then the api call should be successful
    Given I create a fantasy team team1:
      | name         | team |
      | Josh Allen   | BUF  |
      | James Cook   | BUF  |
      | Travis Kelce | KC   |
    Then the response body has:
      | status  | ERROR                                  |
      | message | Team with name 'team1' already exists. |