package com.letsshopecom.stepdefinitions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.apiletsshopecom.clients.ApiClient;
import com.apiletsshopecom.clients.ProductClient;
import com.apiletsshopecom.config.ConfigManager;
import com.apiletsshopecom.payloads.request.AddProductRequest;
import com.apiletsshopecom.payloads.response.AddProductResponse;
import com.github.javafaker.Faker;
import com.letsshopecom.utils.ScenarioContext;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class ProductStepDefinitions {

	private ProductClient productClient;
	private AddProductResponse addProductResponse;
	private Response rawResponse;
	private final ScenarioContext context;

	public ProductStepDefinitions(ScenarioContext context) {
		this.context = context;
	}

	@Given("the user is authorized with a valid token")
	public void the_user_is_authorized_with_a_valid_token() {
		productClient = new ProductClient();

	}

	@When("the user sends a {string} request with form-data to the endpoint {string}")
	public void the_user_sends_a_request_with_form_data_to_the_endpoint(String reqType, String endpoint) {

		if (reqType.equalsIgnoreCase("post") && endpoint.contains("add-product")) {
			Assert.assertEquals(endpoint, productClient.getAddProductEndpoint());

			Map<String, String> productMap = new HashMap<>();

			productMap.put("productName", "iPhone 17 pro max");
			productMap.put("productAddedBy", ConfigManager.getInstance().getProperty("userId"));
			productMap.put("productCategory", "electronics");
			productMap.put("productSubCategory", "mobiles");
			productMap.put("productPrice", "175000");
			productMap.put("productDescription", "iPhone 17 pro max 256 GB");

			File file = new File(System.getProperty("user.dir") + "/src/test/resources/testdata/iphone.jpg");

			rawResponse = productClient.addProduct(endpoint, productMap, file, "productImage");

			System.out.println(rawResponse.asPrettyString());
			addProductResponse = rawResponse.as(AddProductResponse.class);

			context.setRawResponse(rawResponse);

		}

	}

	@Then("the response should contain a {string}")
	public void the_response_should_contain_a(String productId) {
		Assert.assertNotNull(productId, addProductResponse.getProductId());
	}

}
