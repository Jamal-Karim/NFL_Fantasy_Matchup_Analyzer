Feature: Team feature

  Scenario: Create a single fantasy team
    Given I create a fantasy team Cucumber team:
      | name            | team |
      | Patrick Mahomes | KC   |
      | Saquon Barkley  | PHI  |
      | Brock Bowers    | LV   |
    Then the api call should be successful
    Then the team should be saved to the database
    And the team id is saved to {team1}
    When I request the team with id {team1}
    And the response body has:
      | data.name          | Cucumber team |
      | data.roster.size() | 3             |

  Scenario: Cannot get team with invalid id
    When I request the team with id 4266
    Then the response body has:
      | status_code | 404                         |
      | status      | ERROR                       |
      | message     | Team with id 4266 not found |

  Scenario: Create a fantasy team with too many qbs
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

  Scenario: Create a fantasy team with too many RBs
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