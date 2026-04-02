package com.letsshopecom.stepdefinitions;

import com.apiletsshopecom.clients.AuthClient;
import com.apiletsshopecom.payloads.request.RegisterRequest;
import com.apiletsshopecom.payloads.response.RegisterResponse;
import com.github.javafaker.Faker;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import static org.junit.Assert.*;

public class AuthStepDefinitions {

	private AuthClient authClient;
	private RegisterRequest registerRequest;
	private RegisterResponse registerResponse;
	private Response registerRawResponse;

	public AuthStepDefinitions() {

	}

	@Given("the visitor has provided all the valid registration details {string},{string},{string},{string},{string},{string},{string},{string},{string},{string}")
	public void the_visitor_has_provided_all_the_valid_registration_details_true(String firstName, String lastName,
			String userEmail, String userRole, String occupation, String gender, String userMobile, String userPassword,
			String confirmPassword, String required) {

		Faker fake = new Faker();

		if (userEmail.equalsIgnoreCase("dynamic")) {
			userEmail = fake.letterify(userEmail + "???????") + "@test.com";
			System.out.println("Dynamically created email for the user " + firstName + " : " + userEmail);
		}

		registerRequest = new RegisterRequest();
		registerRequest.setFirstName(firstName);
		registerRequest.setLastName(lastName);
		registerRequest.setUserEmail(userEmail);
		registerRequest.setUserRole(userRole);
		registerRequest.setOccupation(occupation);
		registerRequest.setGender(gender);
		registerRequest.setUserMobile(userMobile);
		registerRequest.setUserPassword(userPassword);
		registerRequest.setConfirmPassword(confirmPassword);
		registerRequest.setRequired(Boolean.parseBoolean(required));

		this.authClient = new AuthClient();

	}

	@When("the visitor sends a {string} request to the endpoint {string}")
	public void the_visitor_sends_a_request_to_the_endpoint(String reqType, String endpoint) {

		if (reqType.equalsIgnoreCase("post")) {
			Assert.assertEquals(authClient.getAuthRegisterEndpoint(), endpoint);
			registerRawResponse = authClient.getRegisterUserRawResponse(registerRequest);
			registerResponse = registerRawResponse.as(RegisterResponse.class);
		}

	}

	@Then("the API should respond with status code {int}")
	public void the_api_should_respond_with_status_code(Integer statusCode) {
		Assert.assertEquals(statusCode.intValue(), registerRawResponse.statusCode());

	}

	@Then("the response message should be {string}")
	public void the_response_message_should_be(String string) {
		Assert.assertEquals(string, registerResponse.getMessage());

	}

	@Given("the visitor provides incomplete registration details with missing {string}")
	public void the_visitor_provides_incomplete_registration_details_with_missing(String field) {
		the_visitor_has_provided_all_the_valid_registration_details_true("Jordan", "Smith",
				"jordan.test" + System.currentTimeMillis() + "@example.com", "Customer", "Engineer", "Male",
				"9876543210", "SecurePass123!", "SecurePass123!", "true");

		if (field.equalsIgnoreCase("firstName")) {
			registerRequest.setFirstName("");
		} else if (field.equalsIgnoreCase("userEmail")) {
			registerRequest.setUserEmail("");
		} else if (field.equalsIgnoreCase("userMobile")) {
			registerRequest.setUserMobile("");
		}

	}

	@Then("the response should contain an error indicating that the {string} is required")
	public void the_response_should_contain_an_error_indicating_that_the_is_required(String field) {

		if (field.equalsIgnoreCase("firstName")) {
			Assert.assertEquals(registerResponse.getError(), "First Name is required!");
		} else if (field.equalsIgnoreCase("userEmail")) {
			Assert.assertEquals(registerResponse.getError(), "Email is required!");
		} else if (field.equalsIgnoreCase("userMobile")) {
			Assert.assertEquals(registerResponse.getError(), "Phone Number is required!");
		}

	}

}
