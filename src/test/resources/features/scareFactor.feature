Feature: Player Scare Factor Analysis

  Background:
    Given I fetch the player Josh Allen on team BUF
    Then the api call should be successful
    And the player id is saved to {id1}
    Then the player should be saved to the database

  Scenario: Calculate Scare Factor for a known Elite QB
    When I request the Scare Factor for Josh Allen
    Then the api call should be successful
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

  Scenario: Fetch multiple players
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