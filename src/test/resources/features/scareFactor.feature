Feature: Player Scare Factor Analysis

  Scenario: Calculate Scare Factor for a known Elite QB
    Given I fetch the player Josh Allen on team BUF
    When I request the Scare Factor for Josh Allen
#    Then the response status code should be 200
#    And the player "name" should be "Josh Allen"
#    And the "scare_factor" should be greater than 90
#    And the player should be persisted in the database