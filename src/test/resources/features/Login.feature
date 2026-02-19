Feature: User Authentication and Login

  Background:
    Given User navigates to the login page

  # ============ SMOKE / CRITICAL TESTS ============

  @smoke @regression
  Scenario: Successful login with valid standard user
    When User logs in with credentials "standard_user" and "secret_sauce"
    Then User should see the products page

  @smoke
  Scenario: Unsuccessful login with invalid credentials shows error
    When User logs in with credentials "invalid_user" and "wrong_password"
    Then User should see error message "do not match any user"

  # ============ PARAMETERISED LOGIN VALIDATION ============

  @regression
  Scenario Outline: Login validation across multiple user types
    When User logs in with credentials "<username>" and "<password>"
    Then User should see "<expected_result>"

    Examples:
      | username         | password      | expected_result       |
      | standard_user    | secret_sauce  | products page         |
      | locked_out_user  | secret_sauce  | locked out            |
      | invalid_user     | wrong_pass    | do not match any user |

  # ============ EDGE CASES & FORM VALIDATION ============

  @regression
  Scenario: Login form rejects submission with empty username
    When User enters password "secret_sauce"
    And User clicks the login button
    Then User should see error message "required"

  @regression
  Scenario: Login form rejects submission with empty password
    When User enters username "standard_user"
    And User clicks the login button
    Then User should see error message "required"

  @regression
  Scenario: Logout returns user to the login page
    When User logs in with credentials "standard_user" and "secret_sauce"
    Then User should see the products page
    When User clicks the logout button
    Then User should see the login page

  # ============ PERFORMANCE ============

  @regression
  Scenario: Login completes within a reasonable time limit
    When User logs in with credentials "standard_user" and "secret_sauce"
    Then Login should complete within 10 seconds
    And User should see the products page

  # ============ SECURITY ============

  @regression
  Scenario: Browser back button after login does not expose the authenticated page
    When User logs in with credentials "standard_user" and "secret_sauce"
    Then User should see the products page
    When User clicks the browser back button
    Then User should be redirected to login page
