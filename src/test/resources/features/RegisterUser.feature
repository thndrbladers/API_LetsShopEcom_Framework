Feature: User Registration
  As a new visitor,
  I want to register an account with my personal details
  So that I can log in, access the product catalog, and place orders.

  Scenario Outline: Successfully register a new user
    Given the visitor has provided all the valid registration details "<firstName>","<lastName>","<userEmail>","<userRole>","<occupation>","<gender>",<userMobile>,"<userPassword>","<confirmPassword>","<required>"
    When the visitor sends a "POST" request to the endpoint "/api/ecom/auth/register"
    Then the API should respond with status code 200
    And the response message should be "Registered Successfully"

    Examples:
      | firstName | lastName | userEmail             | userRole | occupation | gender | userMobile | userPassword | confirmPassword | required |
      | John      | Doe      | johndoe@example.com   | customer | Engineer   | Male   | 9876543210 | Pass@123     | Pass@123        | true     |
      | Jane      | Smith    | janesmith@example.com | customer | Doctor     | Female | 9123456789 | Secure@456   | Secure@456      | true     |
