Feature: Checkout

  @Negative
  Scenario Outline: User cannot checkout product
    Given product is already in the cart
    When user click Checkout button
    Then user input <firstname> as firstname
    And user input <lastname> as lastname
    And user input <postalcode> as postal code
    Then user click Continue button
    Then user verify <status> as information

    Examples:
      | firstname | lastname | postalcode | status             |
      |           | Doe      | 12345      | invalid_firstname  |
      | Jhon      |          | 12345      | invalid_lastname   |
      | Jhon      | Doe      |            | invalid_postal     |

  @Positive
  Scenario Outline: User can checkout product
    Given product is already in the cart
    When user click Checkout button
    Then user input <firstname> as firstname
    And user input <lastname> as lastname
    And user input <postalcode> as postal code
    Then user click Continue button
    Then user verify <status> as information
    Then user click Finish button
    Then user verify <status> checkout result

    Examples:
    | firstname | lastname | postalcode | status             |
    | Jhon      | Doe      | 12345      | success            |
