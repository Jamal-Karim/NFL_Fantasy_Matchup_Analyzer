@Team @Roster
Feature: Detailed Roster Construction Rules

  Scenario: Verify Flex slot allows an extra Running Back
    Given I create a fantasy team RB Power:
      | name                | team |
      | Patrick Mahomes     | KC   |
      | Saquon Barkley      | PHI  |
      | Christian McCaffrey | SF   |
      | Breece Hall         | NYJ  |
    Then the api call should be successful
    And I save the response id to {rb_team}
    And the response body has:
      | data.roster.size() | 4 |

  Scenario: Verify Flex slot allows an extra Tight End
    Given I create a fantasy team TE Heavy:
      | name         | team |
      | Jalen Hurts  | PHI  |
      | Brock Bowers | LV   |
      | Travis Kelce | KC   |
    Then the api call should be successful
    And the response body has:
      | data.roster.size() | 3 |

  Scenario: Failure when exceeding position limits and Flex is full
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
    And I save the response id to {full_team}
    When I update the team {full_team}:
      | name                | team |
      | Patrick Mahomes     | KC   |
      | Saquon Barkley      | PHI  |
      | Christian McCaffrey | SF   |
      | Justin Jefferson    | MIN  |
      | Jaylen Waddle       | MIA  |
      | Brock Bowers        | LV   |
      | Breece Hall         | NYJ  |
      | Travis Kelce        | KC   |
    Then the response body has:
      | status_code | 400                                                   |
      | status      | ERROR                                                 |
      | message     | Roster full: Both the TE and Flex slots are occupied. |