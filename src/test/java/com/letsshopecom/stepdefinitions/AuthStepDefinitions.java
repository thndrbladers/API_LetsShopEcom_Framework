package com.letsshopecom.stepdefinitions;

import com.apiletsshopecom.clients.AuthClient;
import com.apiletsshopecom.payloads.request.LoginRequest;
import com.apiletsshopecom.payloads.request.RegisterRequest;
import com.apiletsshopecom.payloads.response.RegisterResponse;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import static org.junit.Assert.*;

public class AuthStepDefinitions {

	private AuthClient authCLient;
	private RegisterRequest registerRequest;
	private RegisterResponse registerResponse;
	private Response registerRawResponse;

	public AuthStepDefinitions() {

	}

	@Given("the visitor has provided all the valid registration details {string},{string},{string},{string},{string},{string},{int},{string},{string},{string}")
	public void the_visitor_has_provided_all_the_valid_registration_details_true(String firstName, String lastName,
			String userEmail, String userRole, String occupation, String gender, int userMobile,
			String userPassword, String confirmPassword, String required) {

		registerRequest = new RegisterRequest();
		registerRequest.setFirstName(firstName);
		registerRequest.setLastName(lastName);
		registerRequest.setUserEmail(userEmail);
		registerRequest.setUserRole(userRole);
		registerRequest.setOccupation(occupation);
		registerRequest.setGender(gender);
		registerRequest.setUserEmail(userEmail);
		registerRequest.setUserMobile(userMobile);
		registerRequest.setUserPassword(userPassword);
		registerRequest.setConfirmPassword(confirmPassword);
		registerRequest.setRequired(Boolean.parseBoolean(required));

		this.authCLient = new AuthClient();

	}

	@When("the visitor sends a {string} request to the endpoint {string}")
	public void the_visitor_sends_a_request_to_the_endpoint(String reqType, String endpoint) {

		if (reqType.equalsIgnoreCase("post")) {
			Assert.assertEquals(authCLient.getAuthRegisterEndpoint(), endpoint);
			registerRawResponse = authCLient.getRegisterUserRawResponse(registerRequest);
			registerResponse = authCLient.getRegisterUserResponse(registerRequest);
		}

	}

	@Then("the API should respond with status code {int}")
	public void the_api_should_respond_with_status_code(Integer statusCode) {
		Assert.assertEquals(registerRawResponse.statusCode(), statusCode.intValue());

	}

	@Then("the response message should be {string}")
	public void the_response_message_should_be(String string) {
		Assert.assertEquals(registerResponse.getMessage(),"Registered Successfully");

	}

}
