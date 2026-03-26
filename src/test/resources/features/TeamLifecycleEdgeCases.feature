@Team @Lifecycle
Feature: Team Lifecycle and State Integrity

  Scenario: Verify players become team-less when their team is deleted
    Given I create a fantasy team Temporary Team:
      | name            | team |
      | Patrick Mahomes | KC   |
      | Saquon Barkley  | PHI  |
    Then the api call should be successful
    And I save the response id to {tempTeam}
    And Patrick Mahomes should be on team {tempTeam}

    When I delete the team with id {tempTeam}
    Then the api call should be successful
    And Patrick Mahomes should not be on team {tempTeam}
    And the team Temporary Team should not exist in the database

  Scenario: Rename an existing team while keeping the same roster
    Given I create a fantasy team Initial Name:
      | name       | team |
      | Josh Allen | BUF  |
    Then the api call should be successful
    And I save the response id to {renameTeam}

    When I update the team {renameTeam} to New Professional Name:
      | name       | team |
      | Josh Allen | BUF  |
    Then the api call should be successful
    And the response body has:
      | data.name | New Professional Name |

    When I request the team with id {renameTeam}
    Then the response body has:
      | data.name | New Professional Name |

  Scenario: Emptying a team roster via update
    Given I create a fantasy team Roster To Clear:
      | name          | team |
      | Lamar Jackson | BAL  |
    Then the api call should be successful
    And I save the response id to {clearTeam}

    When I update the team {clearTeam}:
      | name | team |
    Then the api call should be successful
    And the response body has:
      | data.roster.size() | 0 |
    And Lamar Jackson should not be on team {clearTeam}