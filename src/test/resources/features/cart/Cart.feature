Feature: Manage Shopping Cart
As an authenticated user, 
I want to add and remove items from my cart 
So that I can stage my items before finalizing a purchase.

  @Regression
  Scenario: Add a product to the cart
    Given the user is authorized with a valid token
    And the user provides the following product details:
      | _id                | 6960eae1c941646b7a8b3ed3                                                       |
      | productName        | ADIDAS ORIGINAL                                                                |
      | productCategory    | electronics                                                                    |
      | productSubCategory | mobiles                                                                        |
      | productPrice       | 11500                                                                          |
      | productDescription | Apple phone                                                                    |
      | productImage       | https://rahulshettyacademy.com/api/ecom/uploads/productImage_1767959265156.jpg |
      | productRating      | 0                                                                              |
      | productTotalOrders | 0                                                                              |
      | productStatus      | true                                                                           |
      | productFor         | women                                                                          |
      | productAddedBy     | admin                                                                          |
      | __v                | 0                                                                              |
    When the user sends a "POST" request to the cart endpoint "/api/ecom/user/add-to-cart"
    Then the API should respond with status code 200
    And the response message should be "Product Added To Cart"
