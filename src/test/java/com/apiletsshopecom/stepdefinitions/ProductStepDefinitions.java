package com.apiletsshopecom.stepdefinitions;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.apiletsshopecom.clients.ApiClient;
import com.apiletsshopecom.clients.ProductClient;
import com.apiletsshopecom.config.ConfigManager;
import com.apiletsshopecom.payloads.request.AddProductRequest;
import com.apiletsshopecom.payloads.response.AddProductResponse;
import com.apiletsshopecom.payloads.response.GetProductsResponse;
import com.apiletsshopecom.payloads.response.Product;
import com.apiletsshopecom.utils.ScenarioContext;
import com.github.javafaker.Faker;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class ProductStepDefinitions {

	private ProductClient productClient;
	private AddProductResponse addProductResponse;
	private Response rawResponse;
	private final ScenarioContext context;
	private GetProductsResponse getProductsResponse;

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
			productMap.put("productFor", "men");
			productMap.put("productDescription", "iPhone 17 pro max 256 GB");

			File file = new File(System.getProperty("user.dir") + "/src/test/resources/testdata/iphone.jpg");

			rawResponse = productClient.addProduct(endpoint, productMap, file, "productImage");

			System.out.println(rawResponse.asPrettyString());
			addProductResponse = rawResponse.as(AddProductResponse.class);

			context.setRawResponse(rawResponse);

		}

	}

	@When("the user sends a {string} get all products request to the endpoint {string}")
	public void the_user_sends_a_get_all_products_request_to_the_endpoint(String reqType, String endpoint) {

		if (reqType.equalsIgnoreCase("post") && endpoint.contains("get-all-products")) {
			Assert.assertEquals(endpoint, productClient.getAllProductEndpoint());

			getProductsResponse = new GetProductsResponse();

			rawResponse = productClient.getProductsRawResponse();
			context.setRawResponse(rawResponse);

			System.out.println(rawResponse.asPrettyString());
			getProductsResponse = rawResponse.as(GetProductsResponse.class);

		}
	}

	@Then("the response should contain a {string}")
	public void the_response_should_contain_a(String productId) {
		Assert.assertNotNull(productId, addProductResponse.getProductId());
	}

	@Then("the response should contain a list of products")
	public void the_response_should_contain_a_list_of_products() {

		Assert.assertNotNull(getProductsResponse);

	}

	@Then("each product should have a valid _id, productName, and productPrice")
	public void each_product_should_have_a_valid__id_product_name_and_product_price() {
		List<Product> product = getProductsResponse.getData();

		Iterator<Product> productIT = product.iterator();
		while (productIT.hasNext()) {
			Product prod = productIT.next();
			Assert.assertNotNull(prod.get_id());
			Assert.assertNotNull(prod.getProductName());
			Assert.assertNotNull(Integer.valueOf(prod.getProductPrice()));

		}

	}

}
