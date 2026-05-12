package com.apiletsshopecom.stepdefinitions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.apiletsshopecom.clients.CartClient;
import com.apiletsshopecom.config.ConfigManager;
import com.apiletsshopecom.payloads.request.AddToCartRequest;
import com.apiletsshopecom.payloads.response.Product;
import com.apiletsshopecom.payloads.response.GetCartProductsResponse;
import com.apiletsshopecom.utils.ScenarioContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class CartStepDefinitions {

	private CartClient cartClient;
	private Response rawResponse;
	private final ScenarioContext context;
	private static ConfigManager instance;
	private AddToCartRequest addToCart;
	private Product requestProduct;
	private GetCartProductsResponse getCartResponse;

	public CartStepDefinitions(ScenarioContext context) {
		this.context = context;
		this.cartClient = new CartClient();
		this.instance = ConfigManager.getInstance();
	}

	// IMP
	@Given("the user provides the following product details:")
	public void the_user_provides_the_following_product_details(DataTable dataTable) {

		Map<String, String> productDataMap = dataTable.asMap(String.class, String.class);
		ObjectMapper om = new ObjectMapper();
		requestProduct = om.convertValue(productDataMap, Product.class);

		addToCart = new AddToCartRequest();

		addToCart.set_id(instance.getProperty("userId"));
		addToCart.setProduct(requestProduct);

	}

	@When("the user sends a {string} request to the  add-to-cart endpoint {string}")
	public void the_user_sends_a_request_to_the_add_to_cart_endpoint(String reqType, String endpoint)
			throws InterruptedException {

		if (reqType.equalsIgnoreCase("POST") && endpoint.equalsIgnoreCase(cartClient.getAddToCartEndpoint())) {
			rawResponse = cartClient.addToCartRaw(addToCart);

			context.setRawResponse(rawResponse);
			System.out.println("Status code" + rawResponse.statusCode());
		} else {
			throw new InterruptedException("Request Type and endpoint doesn't match for Add to cart feature");
		}

	}

	/*
	 * @Given("the user has items in their cart:") public void
	 * the_user_has_items_in_their_cart(DataTable dataTable) throws
	 * InterruptedException {
	 * the_user_provides_the_following_product_details(dataTable);
	 * 
	 * }
	 */

	@Given("the user has items in their cart:")
	public void the_user_has_items_in_their_cart(DataTable dataTable) throws InterruptedException {

		the_user_provides_the_following_product_details(dataTable);
		the_user_sends_a_request_to_the_add_to_cart_endpoint("POST", cartClient.getAddToCartEndpoint());
	}

	@When("the user sends a {string} request to the get-cart-products endpoint {string}")
	public void the_user_sends_a_request_to_the_get_cart_products_endpoint(String reqType, String endpoint)
			throws InterruptedException {

		if (reqType.equalsIgnoreCase("GET") && endpoint.equalsIgnoreCase(cartClient.getCartProductsEndpoint())) {

			rawResponse = cartClient.getViewCartRaw(instance.getProperty("userId"));

			context.setRawResponse(rawResponse);

			System.out.println("Status code" + rawResponse.statusCode());
		} else {
			throw new InterruptedException("Request Type and endpoint doesn't match for Get cart feature");
		}

	}

	@Then("the response should display the correct list of products currently in the cart")
	public void the_response_should_display_the_correct_list_of_products_currently_in_the_cart() {

		String expectedProductName = addToCart.getProduct().getProductName();

		getCartResponse = rawResponse.as(GetCartProductsResponse.class);

		List<Product> listProducts = getCartResponse.getProducts();
		List<String> actualProductNameList = new ArrayList<>();

		for (Product p : listProducts) {
			actualProductNameList.add(p.getProductName());
		}

		Assert.assertTrue(actualProductNameList.contains(expectedProductName));

	}

}
