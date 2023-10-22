Feature: Logout
  Scenario: User logout from application
    Given User is already logged in application
    When User click burger menu
    Then User click logout menu
    Then User verify status logout