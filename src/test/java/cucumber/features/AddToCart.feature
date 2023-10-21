Feature: Add To Cart

  Scenario: Add Product to Cart
    Given User is already login
    When User click Add to cart button
    And User click Cart icon
    Then User verify status the cart