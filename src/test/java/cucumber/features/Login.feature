Feature: Login

  @TDD
  Scenario Outline: Ensure login functionality
    Given user is on SauceDemo login page
    When user input <username> as username
    And user input <password> as password
    And user click login button
    Then user verify <status> login result

    Examples:
    | username      | password     | status          |
    | failed_login  | failed_login | failed          |
    |               | secret_sauce | user_empty      |
    | standard_user |              | password_empty  |
    | standard_user | secret_sauce | success         |

