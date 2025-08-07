Feature: POST service of rooms

  Scenario Outline:
    Given Data is given through excel file <excel>
    And Data is read and validated

    Examples:
      | excel                               |
      | "src/test/resources/PostRooms.xlsx" |