package com.letsshopecom.utils;

import com.apiletsshopecom.payloads.request.RegisterRequest;
import com.github.javafaker.Faker;

public class TestDataGenerator {

	private static final Faker fake = new Faker();

	private static final String[] ROLES = { "customer", "vendor" };
	private static final String[] OCCUPATIONS = { "Doctor", "Engineer", "Consultant", "Researcher", "Student" };
	private static final String[] GENDERS = { "Male", "Female" };

	public static RegisterRequest generateRegisterUserData() {

		String password = fake.internet().password(8, 16, true, true);

		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setFirstName(fake.name().firstName());
		registerRequest.setLastName(fake.name().lastName());
		registerRequest.setUserEmail(fake.internet().emailAddress());
		registerRequest.setUserRole(ROLES[fake.random().nextInt(ROLES.length)]);
		registerRequest.setOccupation(OCCUPATIONS[fake.random().nextInt(OCCUPATIONS.length)]);
		registerRequest.setGender(GENDERS[fake.random().nextInt(GENDERS.length)]);
		registerRequest.setUserMobile(fake.numerify("9#########"));
		registerRequest.setUserPassword(password);
		registerRequest.setConfirmPassword(password);
		registerRequest.setRequired(true);

		return registerRequest;
	}

	public static RegisterRequest generateRegisterUserData(String email) {

		RegisterRequest registerRequest = generateRegisterUserData();
		registerRequest.setUserEmail(email);
		return registerRequest;
	}

}