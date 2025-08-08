Feature: Get room (service)

  Scenario Outline: Get room with valid test data
    Given Data is given through excel file <excel>
    Then Data is processed and room given

    Examples:
      | excel                              |
      | "src/test/resources/GetRooms.xlsx" |