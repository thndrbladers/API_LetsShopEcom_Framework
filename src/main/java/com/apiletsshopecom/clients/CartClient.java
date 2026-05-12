package com.apiletsshopecom.clients;

import java.util.Map;

import com.apiletsshopecom.payloads.request.AddToCartResponse;

import io.restassured.response.Response;

public class CartClient {

	private final static String ADD_TO_CART_ENDPOINT = "/api/ecom/user/add-to-cart";

	private final static String VIEW_CART = "/api/ecom/user/get-cart-products/";

	private ApiClient apiClient;

	public CartClient() {
		apiClient = new ApiClient();
		apiClient.withAuthDefaultTestAccount();
	}

	public String getAddToCartEndpoint() {
		return ADD_TO_CART_ENDPOINT;
	}

	public String getViewCart() {
		return VIEW_CART;
	}

	public Response addToCartRaw(Object body) {

		return apiClient.post(ADD_TO_CART_ENDPOINT, body);

	}

	public Response getViewCartRaw(String userId) {
		return apiClient.get(VIEW_CART + userId);
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
