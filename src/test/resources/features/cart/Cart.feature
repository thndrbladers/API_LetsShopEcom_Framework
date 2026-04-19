Feature: Manage Shopping Cart
As an authenticated user, 
I want to add and remove items from my cart 
So that I can stage my items before finalizing a purchase.

  Scenario: Add a product to the cart
    Given the user is authorized with a valid token
    And the user provides a valid "_id" (productId) and "userEmail"
    When the user sends a "POST" request to the endpoint "/api/ecom/user/add-to-cart"
    Then the API should respond with status code 200
    And the response message should be "Product Added To Cart"
