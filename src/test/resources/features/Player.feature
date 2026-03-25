Feature: Player Scare Factor Analysis

  Background:
    Given I fetch the player Josh Allen on team BUF
    Then the api call should be successful
    And the player id is saved to {id1}
    Then the player should be saved to the database

  Scenario: Calculate Scare Factor for a known Elite QB
    When I request the Scare Factor for Josh Allen
    Then the api call should be successful
    Then the scare score should be saved to the database
    Then the scare factor should be greater than 90

  Scenario: Fetch a player that does not exist
    Given I fetch the player Player NA on team BUF
    Then the response body has:
      | status_code | 404                                       |
      | status      | ERROR                                     |
      | message     | Player 'Player NA' not found on team: BUF |

  Scenario: Fetch scare result for player with id
    When I request the Scare Factor for player with id {id1}
    Then the api call should be successful
    Then the scare factor should be greater than 90

  Scenario: Fetch scare result for player with id that does not exist
    When I request the Scare Factor for player with id 4266
    Then the response body has:
      | status_code | 404                   |
      | status      | ERROR                 |
      | message     | Player does not exist |

  Scenario: Fetch multiple players and verify sorting and filtering
    Given I fetch the players:
      | name            | nfl_team |
      | Patrick Mahomes | KC       |
      | Saquon Barkley  | PHI      |
      | Brock Bowers    | LV       |
    Then the api call should be successful
    When I get all players
    Then the response body has:
      | status             | SUCCESS |
      | data.totalElements | 4       |
    And the players are sorted by Scare Factor descending
    When I get all players with position QB
    Then the response body has:
      | status             | SUCCESS |
      | data.totalElements | 2       |

  Scenario: Verify detailed player attributes for Patrick Mahomes
    Given I fetch the player Patrick Mahomes on team KC
    Then the api call should be successful
    And the response body has:
      | data.name      | Patrick Mahomes |
      | data.nfl_team  | KC              |
      | data.position  | QB              |
      | data.is_rookie | false           |
    And the player should be saved to the database

  Scenario: Filter players by Running Back position and verify persistence
    Given I fetch the player Christian McCaffrey on team SF
    When I get all players with position RB
    Then the api call should be successful
    And the response body has:
      | status             | SUCCESS |
      | data.totalElements | 1       |
    And the player should be saved to the database

  Scenario: Verify Scare Factor analysis for a Tight End
    Given I fetch the player Brock Bowers on team LV
    When I request the Scare Factor for Brock Bowers
    Then the api call should be successful
    And the scare score should be saved to the database
    And the scare factor should be greater than 70

  Scenario: Filter players by an invalid position
    When I get all players with position INVALID
    Then the response body has:
      | status_code | 400   |
      | status      | ERROR |

  Scenario: Fetch scare factor for invalid ID type
    When I request the Scare Factor for player with id abc
    Then the response body has:
      | status_code | 404 |

  Scenario: Verify pagination for player list
    Given I fetch the players:
      | name            | nfl_team |
      | Patrick Mahomes | KC       |
      | Saquon Barkley  | PHI      |
      | Brock Bowers    | LV       |
    When I get all players with page 0 and size 2
    Then the api call should be successful
    And the response body has:
      | data.numberOfElements | 2 |
      | data.totalElements    | 4 |