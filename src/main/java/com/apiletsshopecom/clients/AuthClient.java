package com.apiletsshopecom.clients;

import com.apiletsshopecom.payloads.request.LoginRequest;
import com.apiletsshopecom.payloads.response.LoginResponse;

import io.restassured.response.Response;

public class AuthClient {

	private final ApiClient apiClient;

	private final static String AUTH_REGISTER_ENDPOINT = "/api/ecom/auth/register";
	private final static String AUTH_LOGIN_ENDPOINT = "/api/ecom/auth/login";

	public AuthClient() {
		apiClient = new ApiClient();
	}

	public String getJwtToken(LoginRequest loginRequest) {

		return apiClient.post(AUTH_LOGIN_ENDPOINT, loginRequest).jsonPath().getString("token");
	}

	public LoginResponse getLoginResponse(LoginRequest loginRequest) {

		return apiClient.post(AUTH_LOGIN_ENDPOINT, loginRequest).as(LoginResponse.class);
	}

}
