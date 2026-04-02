package com.apiletsshopecom.clients;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;

import com.apiletsshopecom.config.ConfigManager;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiClient {

	private final RequestSpecification requestSpec;
	public static PrintStream log;

	public ApiClient() {
		ConfigManager config = ConfigManager.getInstance();

		try {
			log = new PrintStream(new FileOutputStream("logFile.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		RequestSpecBuilder spec = new RequestSpecBuilder().setBasePath(config.getBaseUrl())
				.setContentType(ContentType.JSON).setAccept(ContentType.JSON)
				.addFilter(RequestLoggingFilter.logRequestTo(log)).addFilter(ResponseLoggingFilter.logResponseTo(log));

		requestSpec = spec.build();

	}

	public RequestSpecification given() {
		RequestSpecification spec = RestAssured.given().spec(requestSpec);
		return spec;

	}

	// ── GET ──────────────────────────────────────────────────────────────

	public Response get(String endpoint) {

		return given().when().post(endpoint).then().extract().response();

	}

	public Response get(String endpoint, Map<String, ?> queryParams) {

		return given().queryParams(queryParams).when().post(endpoint).then().extract().response();

	}

	public Response get(String endpoint, Map<String, ?> queryParams, Map<String, String> headers) {

		return given().queryParams(queryParams).headers(headers).when().post(endpoint).then().extract().response();

	}

	// ── POST ──────────────────────────────────────────────────────────────

	public Response post(String endpoint, String body) {

		return given().body(body).when().post(endpoint).then().extract().response();

	}

	public Response post(String endpoint, Object body) {

		return given().body(body).when().post(endpoint).then().extract().response();

	}

	public Response post(String endpoint, Object body, Map<String, String> headers) {

		return given().headers(headers).body(body).when().post(endpoint).then().extract().response();

	}

	public Response post(String endpoint, Map<String, ?> body, Map<String, String> headers) {

		return given().headers(headers).body(body).when().post(endpoint).then().extract().response();

	}

	// ── PUT ──────────────────────────────────────────────────────────────

	public Response put(String endpoint, String body) {

		return given().body(body).when().put(endpoint).then().extract().response();

	}

	public Response put(String endpoint, Object body) {

		return given().body(body).when().put(endpoint).then().extract().response();

	}

	public Response put(String endpoint, Object body, Map<String, String> headers) {

		return given().headers(headers).body(body).when().put(endpoint).then().extract().response();

	}

	public Response put(String endpoint, Map<String, ?> body, Map<String, String> headers) {

		return given().headers(headers).body(body).when().put(endpoint).then().extract().response();

	}

	// ── Delete ──────────────────────────────────────────────────────────────
	public Response delete(String endpoint) {

		return given().when().delete(endpoint).then().extract().response();

	}

	public Response delete(String endpoint, String body) {

		return given().body(body).when().delete(endpoint).then().extract().response();

	}

	public Response delete(String endpoint, Object body) {

		return given().body(body).when().delete(endpoint).then().extract().response();

	}

	public Response delete(String endpoint, Object body, Map<String, String> headers) {

		return given().headers(headers).body(body).when().delete(endpoint).then().extract().response();

	}

	public ApiClient withAuth() {
		String token = ConfigManager.getInstance().getAuthToken();
		requestSpec.header("Authorization", "Bearer " + token);
		return this;
	}

	public ApiClient withHeaders(Map<String, String> headers) {
		requestSpec.headers(headers);
		return this;
	}

}
