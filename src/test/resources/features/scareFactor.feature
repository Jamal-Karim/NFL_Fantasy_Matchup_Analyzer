Feature: Player Scare Factor Analysis

  Scenario: Calculate Scare Factor for a known Elite QB
    Given I fetch the player Josh Allen on team BUF
    Then the api call should be successful
    Then the player should be saved to the database
    When I request the Scare Factor for Josh Allen
    Then the api call should be successful
    Then the scare factor should be greater than 90

  Scenario: Fetch a player that does not exist
    Given I fetch the player Player NA on team BUF
    Then the response body has:
      | status_code | 404                                       |
      | status      | ERROR                                     |
      | message     | Player 'Player NA' not found on team: BUF |