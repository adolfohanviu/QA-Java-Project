Feature: User Authentication and Login

  Background:
    Given User navigates to the login page

  @smoke @regression
  Scenario: Successful login with valid credentials
    When User logs in with credentials "standard_user" and "secret_sauce"
    Then User should see the products page

  @smoke
  Scenario: Unsuccessful login with invalid credentials
    When User logs in with credentials "invalid_user" and "wrong_password"
    Then User should see error message "do not match any user"

  @regression
  Scenario: Unsuccessful login with locked user
    When User logs in with credentials "locked_out_user" and "secret_sauce"
    Then User should see error message "locked out"

  @regression
  Scenario: Login form validation
    When User enters username "standard_user"
    And User enters password "secret_sauce"
    And User clicks the login button
    Then User should see the products page
