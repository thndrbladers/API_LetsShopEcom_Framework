package com.apiletsshopecom.clients;

public class CartClient {

	private final static String CART_ENDPOINT = "/api/ecom/user/get-cart";

	private ApiClient apiClient;

	public CartClient() {
		apiClient = apiClient.withAuthDefaultTestAccount();

	}

	public static String getCartEndpoint() {
		return CART_ENDPOINT;
	}

}
