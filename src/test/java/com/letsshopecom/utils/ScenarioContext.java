package com.letsshopecom.utils;

import io.restassured.response.Response;

public class ScenarioContext {
	
	private Response rawResponse;

	public Response getRawResponse() {
		return rawResponse;
	}

	public void setRawResponse(Response rawResponse) {
		this.rawResponse = rawResponse;
	}
	

}
