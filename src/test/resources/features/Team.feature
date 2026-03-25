Feature: Team feature

  Scenario: Create a single fantasy team
    Given I create a fantasy team Cucumber team:
      | name            | team |
      | Patrick Mahomes | KC   |
      | Saquon Barkley  | PHI  |
      | Brock Bowers    | LV   |
    Then the api call should be successful
    And the team id is saved to {team1}

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