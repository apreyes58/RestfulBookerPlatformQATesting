Feature: POST service of rooms

  Scenario Outline:
    Given Data is given through excel file <excel>
    And Data is read
    Then Now check tests if valid

    Examples:
      | excel                               |
      | "src/test/resources/PostRooms.xlsx" |