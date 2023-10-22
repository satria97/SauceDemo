Feature: Remove In Cart

  Scenario: Removing product in the Cart
    Given Products is already in the Cart
    When User click icon Cart
    Then User click Remove button
    Then User verify status remove