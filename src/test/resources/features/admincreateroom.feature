Feature: Create a new rooms as an admin

  Scenario Outline: Create rooms that are available
    Given User goes to "<url>"
    And User clicks to go to Admin Page
    When User logs in as an Admin using <username> and <password>
    Then User creates a room with <name>, <type>, <accessible>, <price>, <features>, <expected>

    Examples:
      | url                    | username | password   | name   | type     | accessible | price | features                  | expected  |
      | http://localhost:3003/ | "admin"  | "password" | "104"  | "Single" | "true"       | 400   | "TV, WiFi, Radio, Safe" | "pass"    |
