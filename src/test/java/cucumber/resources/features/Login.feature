Feature: Login
  Scenario Outline: Ensure login functionality
    Given user is on SauceDemo login page
    When user input <username> as username
    And user input <password> as password
    And user click login button
    Then user verify <status> login result

    Examples:
    | username      | password     | status  |
    | standard_user | secret_sauce | success |
    | failed_login  | failed_login | failed  |