package com.letsshopecom.stepdefinitions;

import com.apiletsshopecom.clients.AuthClient;
import com.apiletsshopecom.payloads.request.LoginRequest;
import com.apiletsshopecom.payloads.request.RegisterRequest;
import com.apiletsshopecom.payloads.response.LoginResponse;
import com.apiletsshopecom.payloads.response.RegisterResponse;
import com.github.javafaker.Faker;
import com.letsshopecom.utils.TestDataGenerator;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;

public class AuthStepDefinitions {

	private AuthClient authClient;
	private RegisterRequest registerRequest;
	private RegisterResponse registerResponse;
	private Response rawResponse;
	private LoginRequest loginRequest;
	private LoginResponse loginResponse;
	/*
	 * private static final Map<String, String> ERROR_MAP = Map.of("firstName",
	 * "First Name is required!", "userEmail", "Email is required!", "userMobile",
	 * "Phone Number is required!");
	 */

	public AuthStepDefinitions() {

	}

	@Given("the visitor has provided all the valid registration details {string},{string},{string},{string},{string},{string},{string},{string},{string},{string}")
	public void the_visitor_has_provided_all_the_valid_registration_details_true(String firstName, String lastName,
			String userEmail, String userRole, String occupation, String gender, String userMobile, String userPassword,
			String confirmPassword, String required) {

		Faker fake = new Faker();

		if (userEmail.equalsIgnoreCase("dynamic")) {
			userEmail = fake.internet().emailAddress();
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

		if (reqType.equalsIgnoreCase("post") && endpoint.contains("register")) {
			Assert.assertEquals(endpoint, authClient.getAuthRegisterEndpoint());
			rawResponse = authClient.getRawResponse(registerRequest);
			registerResponse = rawResponse.as(RegisterResponse.class);
		}
	}

	@Then("the API should respond with status code {int}")
	public void the_api_should_respond_with_status_code(Integer statusCode) {

		Assert.assertEquals(rawResponse.statusCode(), statusCode.intValue());

	}

	@Then("the response message should be {string}")
	public void the_response_message_should_be(String message) {

		Assert.assertEquals(message, rawResponse.jsonPath().getString("message"));

	}

	@Given("the visitor provides incomplete registration details with missing {string}")
	public void the_visitor_provides_incomplete_registration_details_with_missing(String field) {
		the_visitor_has_provided_all_the_valid_registration_details_true("Jordan", "Smith",
				"jordan.test" + System.currentTimeMillis() + "@example.com", "Customer", "Engineer", "Male",
				"9876543210", "SecurePass123!", "SecurePass123!", "true");

		/*
		 * Field f = null; try { f = RegisterRequest.class.getDeclaredField(field); }
		 * catch (NoSuchFieldException e) { e.printStackTrace(); }
		 * f.setAccessible(true); try { f.set(registerRequest, null); } catch
		 * (IllegalArgumentException e) { e.printStackTrace(); } catch
		 * (IllegalAccessException e) { e.printStackTrace(); }
		 */

		if (field.equalsIgnoreCase("firstName")) {
			registerRequest.setFirstName(null);
		} else if (field.equalsIgnoreCase("userEmail")) {
			registerRequest.setUserEmail(null);
		} else if (field.equalsIgnoreCase("userMobile")) {
			registerRequest.setUserMobile(null);
		}

	}

	@Then("the response should contain an error indicating that the {string} is required")
	public void the_response_should_contain_an_error_indicating_that_the_is_required(String field) {

		if (field.equalsIgnoreCase("firstName")) {
			Assert.assertEquals("First Name is required!", registerResponse.getError());
		} else if (field.equalsIgnoreCase("userEmail")) {
			Assert.assertEquals("Email is required!", registerResponse.getError());
		} else if (field.equalsIgnoreCase("userMobile")) {
			Assert.assertEquals("Phone Number is required!", registerResponse.getError());
		}

		/* Assert.assertEquals(ERROR_MAP.get(field), registerResponse.getError()); */

	}

	@Given("the visitor provides registration details using an email that is already registered")
	public void the_visitor_provides_registration_details_using_an_email_that_is_already_registered() {

		the_visitor_has_provided_all_the_valid_registration_details_true("Jordan", "Smith",
				"jordan.test" + System.currentTimeMillis() + "@example.com", "Customer", "Engineer", "Male",
				"9876543210", "SecurePass123!", "SecurePass123!", "true");

		registerResponse = authClient.getRegisterUserResponse(registerRequest);

		System.out.println(registerRequest.getUserEmail());

		the_visitor_has_provided_all_the_valid_registration_details_true("Jordan", "Smith",
				registerRequest.getUserEmail(), "Customer", "Engineer", "Male", "9876543210", "SecurePass123!",
				"SecurePass123!", "true");

		// Other way

		/*
		 * registerRequest = TestDataGenerator.generateRegisterUserData();
		 * this.authClient = new AuthClient(); registerResponse =
		 * authClient.getRegisterUserResponse(registerRequest); String reuseEmail =
		 * registerRequest.getUserEmail(); System.out.println(reuseEmail);
		 * registerRequest = TestDataGenerator.generateRegisterUserData();
		 * registerRequest.setUserEmail(reuseEmail);
		 */

	}

	@Then("the response message should indicate {string}")
	public void the_response_message_should_indicate(String message) {

		Assert.assertEquals(message, registerResponse.getMessage());

	}

	@Given("the user possesses valid login credentials")
	public void the_user_possesses_valid_login_credentials() {

		registerRequest = TestDataGenerator.generateRegisterUserData();
		this.authClient = new AuthClient();
		authClient.getRegisterUserResponse(registerRequest);
		loginRequest = new LoginRequest();

		loginRequest.setUserEmail(registerRequest.getUserEmail());
		loginRequest.setUserPassword(registerRequest.getUserPassword());

	}

	@When("the user sends a {string} request to the endpoint {string}")
	public void the_user_sends_a_request_to_the_endpoint(String reqType, String endpoint) {

		if (reqType.equalsIgnoreCase("post") && endpoint.contains("login")) {
			Assert.assertEquals(endpoint, authClient.getAuthLoginEndpoint());
			rawResponse = authClient.getRawResponse(loginRequest);
			loginResponse = rawResponse.as(LoginResponse.class);
		}

	}

	@Then("the response body should contain a valid JWT {string}, {string} and message {string}")
	public void the_response_body_should_contain_a_valid_jwt_and_message(String token, String userId, String message) {

		Assert.assertNotNull(token);
		Assert.assertNotNull(userId);
		Assert.assertEquals(message, loginResponse.getMessage());

	}

}
