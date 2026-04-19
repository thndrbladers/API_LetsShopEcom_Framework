package com.apiletsshopecom.clients;

import java.util.Map;

import com.apiletsshopecom.payloads.request.AddToCartResponse;

import io.restassured.response.Response;

public class CartClient {

	private final static String ADD_TO_CART_ENDPOINT = "/api/ecom/user/add-to-cart";

	private final static String GET_TO_CART = "/api/ecom/user/get-cart-count";

	private ApiClient apiClient;

	public CartClient() {
		apiClient = new ApiClient();
		apiClient.withAuthDefaultTestAccount();
	}

	public static String getAddToCartEndpoint() {
		return ADD_TO_CART_ENDPOINT;
	}

	public static String getGetToCart() {
		return GET_TO_CART;
	}

	public Response addToCartRaw(Object body) {

		return apiClient.post(ADD_TO_CART_ENDPOINT, body);

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
