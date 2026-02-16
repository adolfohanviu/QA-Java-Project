Feature: Product Shopping and Cart Management

  Background:
    Given User navigates to the login page
    When User logs in with credentials "standard_user" and "secret_sauce"
    Then User should see the products page
    And User is on the products page

  @smoke @regression
  Scenario: Add product to cart
    When User adds product to cart
    Then Product should be added to cart

  @regression
  Scenario: View cart contents
    When User adds product to cart
    And User navigates to cart
    Then User should see 1 items in cart

  @regression
  Scenario: Sort products by price
    When User sorts products by "Price (low to high)"
    Then User is on the products page

  @smoke
  Scenario: Add multiple products to cart
    When User adds product to cart
    And User adds product to cart
    Then Product should be added to cart
