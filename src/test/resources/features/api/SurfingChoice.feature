@surfingchoice @api
Feature: Surfing Choice

  Scenario: Choose a nice day to surf
  	Given I like to surf in the city "Sydney" in country "Australia"
    Given I only like to surf on "Monday & Friday" in the next "16 days"
    When I look up the the weather forecast for the next required time with POSTAL CODES "2095,2034"
    Then I check to see if the temparature is between 20.0 and 30.0 degrees
    And I check the wind speed to be between 5.0 and 9.0
    And I check if the UV index is <= 11.0
    And I pickup the best spots and dates to surf on