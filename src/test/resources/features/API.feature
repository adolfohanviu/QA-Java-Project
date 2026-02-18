Feature: API Testing for User Management

  @api @regression
  Scenario: Get users list from API
    When I make a GET request to "/users"
    Then The response status code should be 200

  @regression @api
  Scenario: Create a new user via API
    When I make a POST request to "/users" with fixture "userBasic"
    Then The response status code should be 201

  @regression @api
  Scenario: Update user via API
    When I make a PUT request to "/users/1" with fixture "userUpdate"
    Then The response status code should be 200

  @regression @api
  Scenario: Delete user via API
    When I make a DELETE request to "/users/1"
    Then The response status code should be 200
