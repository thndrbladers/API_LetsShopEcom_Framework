package com.letsshopecom.runners;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import com.apiletsshopecom.resources.CucumberReports;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = { "com.letsshopecom.stepdefinitions" }, plugin = {
		"json:target/jsonReports/cucumber-report.json", "pretty" })
public class TestRunner {

	@AfterClass
	public static void tearDown() {
		CucumberReports cr = new CucumberReports();
		cr.generateReports();

	}

}
