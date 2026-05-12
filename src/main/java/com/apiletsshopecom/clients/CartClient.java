package com.apiletsshopecom.clients;

import java.util.Map;

import com.apiletsshopecom.payloads.request.AddToCartResponse;
import com.apiletsshopecom.payloads.response.GetCartProductsResponse;

import io.restassured.response.Response;

public class CartClient {

	private final static String ADD_TO_CART_ENDPOINT = "/api/ecom/user/add-to-cart";

	private final static String GET_CART_PRODUCTS = "/api/ecom/user/get-cart-products";

	private ApiClient apiClient;

	public CartClient() {
		apiClient = new ApiClient();
		apiClient.withAuthDefaultTestAccount();
	}

	public String getAddToCartEndpoint() {
		return ADD_TO_CART_ENDPOINT;
	}

	public String getCartProductsEndpoint() {
		return GET_CART_PRODUCTS;
	}

	public Response addToCartRaw(Object body) {

		return apiClient.post(ADD_TO_CART_ENDPOINT, body);

	}

	public Response getViewCartRaw(String userId) {

		System.out.println(userId);
		System.out.println(apiClient.get(GET_CART_PRODUCTS + "/" + userId).asPrettyString());

		return apiClient.get(GET_CART_PRODUCTS + "/" + userId);
	}

	/*
	 * public AddToCartResponse addToCartRaw(Object body) {
	 * 
	 * return apiClient.post(ADD_TO_CART_ENDPOINT,
	 * body).as(AddToCartResponse.class);
	 * 
	 * }
	 */

}
