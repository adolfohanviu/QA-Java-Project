Feature: Product Shopping and Cart Management

  Background:
    Given User navigates to the login page
    When User logs in with credentials "standard_user" and "secret_sauce"
    Then User should see the products page
    And User is on the products page

  # ============ ADD TO CART ============

  @smoke @regression
  Scenario: Adding the first product to cart increments the cart badge
    When User adds product to cart
    Then Product should be added to cart

  @regression
  Scenario: Cart page shows correct item count after adding one product
    When User adds product to cart
    And User navigates to cart
    Then User should see 1 items in cart

  # ============ MULTIPLE PRODUCTS ============

  @smoke
  Scenario: Adding two different products to cart updates badge count to 2
    When User adds product 1 to cart
    And User adds product 2 to cart
    Then Product should be added to cart

  @regression
  Scenario: Cart shows all added products after adding two distinct items
    When User adds product 1 to cart
    And User adds product 2 to cart
    And User navigates to cart
    Then User should see 2 items in cart

  # ============ SORTING ============

  @regression
  Scenario: Sorting products by price low-to-high displays cheapest product first
    When User sorts products by "Price (low to high)"
    Then Products should be sorted by price low to high
