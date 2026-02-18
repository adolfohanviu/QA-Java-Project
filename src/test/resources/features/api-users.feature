Feature: API Testing for User Management

  @api @regression
  Scenario: GET users list returns success with non-empty body
    When I make a GET request to "/users"
    Then The response status code should be 200
    And The response body should not be empty

  @regression @api
  Scenario: POST creates a new user and returns the created resource
    When I make a POST request to "/users" with body:
      | username | automation_user |
      | email    | auto@test.com   |
      | role     | user            |
    Then The response status code should be 201
    And The response body should not be empty
    And The response body field "id" should not be null

  @regression @api
  Scenario: PUT updates an existing user and returns the updated resource
    When I make a PUT request to "/users/1" with body:
      | email | updated@test.com |
    Then The response status code should be 200
    And The response body should not be empty

  @regression @api
  Scenario: DELETE an existing user returns success status
    When I make a DELETE request to "/users/1"
    Then The response status code should be 200
