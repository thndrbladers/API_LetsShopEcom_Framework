package com.apiletsshopecom.clients;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.apiletsshopecom.config.ConfigManager;
import com.apiletsshopecom.payloads.request.AddProductRequest;
import com.apiletsshopecom.payloads.request.LoginRequest;
import com.apiletsshopecom.payloads.response.AddProductResponse;
import com.apiletsshopecom.payloads.response.DeleteProductResponse;
import com.apiletsshopecom.payloads.response.LoginResponse;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ProductClient {

	private final ApiClient apiClient;
	private final static String ADD_PRODUCT_ENDPOINT = "/api/ecom/product/add-product";
	private final static String ALL_PRODUCT_ENDPOINT = "/api/ecom/product/get-all-products";
	private final static String DELETE_PRODUCT = "/api/ecom/product/delete-product";

	public ProductClient() {
		this.apiClient = new ApiClient();
	}

	public Response addProduct(String endpoint, Map<String, String> formParams, File file, String fileFieldName) {

		apiClient.withAuthDefaultTestAccount();

		return apiClient.post(ADD_PRODUCT_ENDPOINT, formParams, file, fileFieldName);
	}

	public String getAddProductEndpoint() {
		return ADD_PRODUCT_ENDPOINT;
	}

	public String getAllProductEndpoint() {
		return ALL_PRODUCT_ENDPOINT;
	}

	public DeleteProductResponse deleteProductResponse(String productId) {

		return deleteProductRawResponse(productId).as(DeleteProductResponse.class);
	}

	public Response deleteProductRawResponse(String productId) {
		apiClient.withAuthDefaultTestAccount();
		return apiClient.delete(DELETE_PRODUCT, productId);
	}

}
