package com.apiletsshopecom.clients;

import com.apiletsshopecom.payloads.request.LoginRequest;
import com.apiletsshopecom.payloads.request.RegisterRequest;
import com.apiletsshopecom.payloads.response.LoginResponse;
import com.apiletsshopecom.payloads.response.RegisterResponse;

import io.restassured.response.Response;

public class AuthClient {

	private final ApiClient apiClient;

	private final static String AUTH_REGISTER_ENDPOINT = "/api/ecom/auth/register";
	private final static String AUTH_LOGIN_ENDPOINT = "/api/ecom/auth/login";

	public AuthClient() {
		apiClient = new ApiClient();
	}

	public String getJwtToken(LoginRequest loginRequest) {

		return getLoginResponse(loginRequest).getToken();

	}

	public LoginResponse getLoginResponse(LoginRequest loginRequest) {

		return apiClient.post(AUTH_LOGIN_ENDPOINT, loginRequest).as(LoginResponse.class);
	}

	public RegisterResponse getRegisterUserResponse(RegisterRequest registerRequest) {

		return apiClient.post(AUTH_REGISTER_ENDPOINT, registerRequest).as(RegisterResponse.class);
	}

	public Response getRegisterUserRawResponse(RegisterRequest registerRequest) {

		return apiClient.post(AUTH_REGISTER_ENDPOINT, registerRequest);
	}

	public String getAuthRegisterEndpoint() {
		return this.AUTH_REGISTER_ENDPOINT;
	}

	public String getAuthLoginEndpoint() {
		return this.AUTH_LOGIN_ENDPOINT;
	}

}
