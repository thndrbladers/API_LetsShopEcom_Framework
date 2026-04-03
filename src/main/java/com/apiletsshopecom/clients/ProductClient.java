package com.apiletsshopecom.clients;

import java.util.HashMap;
import java.util.Map;

import com.apiletsshopecom.payloads.request.AddProductRequest;
import com.apiletsshopecom.payloads.request.LoginRequest;
import com.apiletsshopecom.payloads.response.LoginResponse;

import io.restassured.response.Response;

public class ProductClient {

	private final ApiClient apiClient;
	private final AuthClient authClient;
	private final static String ADD_PRODUCT_ENDPOINT = "/api/ecom/product/add-product";
	private final static String ALL_PRODUCT_ENDPOINT = "/api/ecom/product/get-all-products";

	public ProductClient() {
		this.apiClient = new ApiClient();
		this.authClient = new AuthClient();
	}

	public Response addProduct(String userEmail, String password, AddProductRequest addProductRequest) {

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail(userEmail);
		loginRequest.setUserPassword(password);

		LoginResponse loginResponse = authClient.getLoginResponse(loginRequest);

		String token = loginResponse.getToken();
		String productAddedBy = loginResponse.getUserId();
		if (token == null || productAddedBy == null) {
			throw new RuntimeException("Login failed for user: " + userEmail);
		}

		addProductRequest.setProductFor(productAddedBy);

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", token);

		return apiClient.post(ADD_PRODUCT_ENDPOINT, addProductRequest, headers);
	}

}
