Feature: Player feature

  Background:
    Given I fetch the player Josh Allen on team BUF
    Then the api call should be successful
    And the player id is saved to {id1}
    Then the player should be saved to the database

  Scenario:
    Given I create a fantasy team Cucumber team:
      | name            | team |
      | Patrick Mahomes | KC   |
      | Saquon Barkley  | PHI  |
      | Brock Bowers    | LV   |
    And the team id is saved to {id1}