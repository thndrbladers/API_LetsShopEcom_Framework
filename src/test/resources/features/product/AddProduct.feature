Feature: Browse and Manage Products
As an authenticated user, 
I want to retrieve a list of available products 
So that I can view the catalog and select items to purchase.

  @Regression @Smoke @AddProduct
  Scenario: Add a new product to the catalog (Admin/Setup Scenario)
    Given the user is authorized with a valid token
    When the user sends a "POST" request with form-data to the endpoint "/api/ecom/product/add-product"
    Then the API should respond with status code 201
    And the response should contain a "productId"
    And the response message should be "Product Added Successfully"

  @Regression
  Scenario: Retrieve all available products successfully
    Given the user is authorized with a valid token
    When the user sends a "POST" get all products request to the endpoint "/api/ecom/product/get-all-products"
    Then the API should respond with status code 200
    And the response should contain a list of products
    And each product should have a valid _id, productName, and productPrice
