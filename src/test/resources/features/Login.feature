Feature: As a registered user,
I want to authenticate via the login API 
So that I can receive a secure JWT token to access my account and place orders.

  Scenario: User should be able to login with valid credentials when they hit login API
    Given the user possesses valid login credentials
    When the user sends a "POST" request to the endpoint "/api/ecom/auth/login"
    Then the API should respond with status code 200
    And the response body should contain a valid JWT "token", "userId" and message "Login Successfully"
    And the response message should be "Login Successfully"
