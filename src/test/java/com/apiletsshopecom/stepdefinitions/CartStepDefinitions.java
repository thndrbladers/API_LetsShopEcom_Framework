package com.apiletsshopecom.stepdefinitions;

import java.util.Map;

import com.apiletsshopecom.clients.CartClient;
import com.apiletsshopecom.config.ConfigManager;
import com.apiletsshopecom.payloads.request.AddToCartRequest;
import com.apiletsshopecom.payloads.response.Product;
import com.apiletsshopecom.utils.ScenarioContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class CartStepDefinitions {

	private CartClient cartClient;
	private Response rawResponse;
	private final ScenarioContext context;
	private static ConfigManager instance;
	private AddToCartRequest addToCart;
	private Product requestProduct;

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

	@When("the user sends a {string} request to the cart endpoint {string}")
	public void the_user_sends_a_request_to_the_cart_endpoint(String reqType, String endpoint) {
		rawResponse = cartClient.addToCartRaw(addToCart);

		context.setRawResponse(rawResponse);

	}

}
