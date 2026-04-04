package com.letsshopecom.stepdefinitions;

import org.junit.Assert;

import com.letsshopecom.utils.ScenarioContext;

import io.cucumber.java.en.Then;

public class CommonStepDefinitions {

	private final ScenarioContext context;

	public CommonStepDefinitions(ScenarioContext context) {
		this.context = context;
	}

	@Then("the API should respond with status code {int}")
	public void the_api_should_respond_with_status_code(Integer statusCode) {

		Assert.assertEquals(context.getRawResponse().statusCode(), statusCode.intValue());

	}

	@Then("the response message should be {string}")
	public void the_response_message_should_be(String message) {
		Assert.assertEquals(message, context.getRawResponse().jsonPath().getString("message"));

	}

}
