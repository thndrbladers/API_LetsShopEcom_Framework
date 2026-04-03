package com.apiletsshopecom.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

import net.masterthought.cucumber.Configuration;

import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.presentation.PresentationMode;
import net.masterthought.cucumber.sorting.SortingMethod;

public class CucumberReports {

	public void generateReports() {

//Below commented code is a test code for combining multiple jsons generated from Parallel Testing and combing into one for a combined report generation
//Path where all your parallel JSON files are stored
//		File jsonDirectory = new File("target/jsonReports");
//		Collection<File> listOfJsonFiles = FileUtils.listFiles(jsonDirectory, new String[] { "json" }, true);
//
//		List<String> jsonFiles = new ArrayList<>();
//		for (File file : listOfJsonFiles) {
//			jsonFiles.add(file.getAbsolutePath());
//		}

		File reportOutputDirectory = new File("target/reports");
		List<String> jsonFiles = new ArrayList<>();
		jsonFiles.add("target/jsonReports/cucumber-report.json");

		String buildNumber = "1";
		String projectName = "BDD_APIFramework";
		Configuration configuration = new Configuration(reportOutputDirectory, projectName);
		configuration.setNotFailingStatuses(Collections.singleton(Status.SKIPPED));
		configuration.setBuildNumber(buildNumber);

		configuration.addClassifications("Browser", "Chrome");
		configuration.addClassifications("Branch", "release/1.0");
		configuration.setSortingMethod(SortingMethod.NATURAL);
		configuration.addPresentationModes(PresentationMode.EXPAND_ALL_STEPS);
		configuration.addPresentationModes(PresentationMode.PARALLEL_TESTING);
		configuration.setQualifier("API", "HTTP, WINDOWS");

		ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
		reportBuilder.generateReports();
	}

}
