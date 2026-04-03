Feature: User Registration
  As a new visitor,
  I want to register an account with my personal details
  So that I can log in, access the product catalog, and place orders.

  Scenario Outline: Successfully register a new user
    Given the visitor has provided all the valid registration details "<firstName>","<lastName>","<userEmail>","<userRole>","<occupation>","<gender>","<userMobile>","<userPassword>","<confirmPassword>","<required>"
    When the visitor sends a "POST" request to the endpoint "/api/ecom/auth/register"
    Then the API should respond with status code 200
    And the response message should be "Registered Successfully"

    Examples:
      | firstName | lastName | userEmail | userRole | occupation | gender | userMobile | userPassword | confirmPassword | required |
      | Aakash v  | Kulkarni | dynamic   | customer | Consultant | Male   | 9321456780 | Nova@258     | Nova@258        | true     |
      | Meera d   | Banerjee | dynamic   | customer | Researcher | Female | 9210345678 | Orion@369    | Orion@369       | true     |

  Scenario Outline: Unsuccessful registration due to missing required fields
    Given the visitor provides incomplete registration details with missing "<field>"
    When the visitor sends a "POST" request to the endpoint "/api/ecom/auth/register"
    Then the API should respond with status code 422
    And the response should contain an error indicating that the "<field>" is required

    Examples:
      | field      |
      | firstName  |
      | userEmail  |
      | userMobile |

  Scenario: Unsuccessful registration with an already existing email
    Given the visitor provides registration details using an email that is already registered
    When the visitor sends a "POST" request to the endpoint "/api/ecom/auth/register"
    Then the API should respond with status code 400
    And the response message should be "User already exisits with this Email Id!"
