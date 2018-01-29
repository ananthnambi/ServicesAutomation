package com.test.services.model.mongo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class for storing test case details
 * 
 * @version 0.0.1
 * 
 */
@Document(collection = "testcases")
public class Testcase {

	private String testId;

	private String testDesc;

	private List<String> inputRules;

	private List<String> outputRules;

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}

	public List<String> getInputRules() {
		return inputRules;
	}

	public void setInputRules(List<String> inputRules) {
		this.inputRules = inputRules;
	}

	public List<String> getOutputRules() {
		return outputRules;
	}

	public void setOutputRules(List<String> outputRules) {
		this.outputRules = outputRules;
	}

}