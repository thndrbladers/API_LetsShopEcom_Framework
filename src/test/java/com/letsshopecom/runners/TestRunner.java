package com.letsshopecom.runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = { "com.letsshopecom.stepdefinitions" }, plugin = {
		"pretty" })
public class TestRunner {

}
