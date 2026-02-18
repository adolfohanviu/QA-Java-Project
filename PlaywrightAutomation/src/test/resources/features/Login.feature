Feature: User Authentication and Login

  Background:
    Given User navigates to the login page

  # ============ SMOKE/CRITICAL TESTS ============
  @smoke @regression
  Scenario: Successful login with valid standard user
    When User logs in with credentials "standard_user" and "secret_sauce"
    Then User should see the products page

  @smoke
  Scenario: Unsuccessful login with invalid credentials
    When User logs in with credentials "invalid_user" and "wrong_password"
    Then User should see error message "do not match any user"

  # ============ PARAMETERIZED TESTS WITH MULTIPLE DATA SETS ============
  @regression
  Scenario Outline: Login validation with multiple user scenarios
    When User logs in with credentials "<username>" and "<password>"
    Then User should see "<expected_result>"

    Examples:
      | username         | password      | expected_result                 |
      | standard_user    | secret_sauce  | products page                   |
      | locked_out_user  | secret_sauce  | locked out                      |
      | problem_user     | secret_sauce  | products page                   |
      | invalid_user     | wrong_pass    | do not match any user           |

  # ============ EDGE CASES & VALIDATION TESTS ============
  @regression
  Scenario: Login form validation with empty username
    When User enters password "secret_sauce"
    And User clicks the login button
    Then User should see error message "required"

  @regression
  Scenario: Login form validation with empty password
    When User enters username "standard_user"
    And User clicks the login button
    Then User should see error message "required"

  @regression
  Scenario: Logout functionality
    When User logs in with credentials "standard_user" and "secret_sauce"
    Then User should see the products page
    When User clicks the logout button
    Then User should see the login page

  # ============ PERFORMANCE/TIMING TESTS ============
  @regression
  Scenario: Login completes within reasonable time
    When User logs in with credentials "standard_user" and "secret_sauce"
    Then Login should complete within 10 seconds
    And User should see the products page

  # ============ BROWSER BACK BUTTON TEST ============
  @regression
  Scenario: Browser back button after login
    When User logs in with credentials "standard_user" and "secret_sauce"
    Then User should see the products page
    When User clicks the browser back button
    Then User should be redirected to login page