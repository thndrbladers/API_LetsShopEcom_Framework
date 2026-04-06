package com.apiletsshopecom.runners;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import com.apiletsshopecom.resources.CucumberReports;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = { "com.apiletsshopecom.stepdefinitions",
		"com.apiletsshopecom.hooks" }, plugin = { "json:target/jsonReports/cucumber-report.json",
				"pretty" }, tags = "@Smoke")
public class TestSmokeRunner {

	@AfterClass
	public static void tearDown() {
		CucumberReports cr = new CucumberReports();
		cr.generateReports();

	}

}
