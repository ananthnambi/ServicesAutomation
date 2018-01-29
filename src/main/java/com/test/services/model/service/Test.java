package com.test.services.model.service;

import java.util.List;

import com.test.services.model.mongo.Rule;

public class Test {

	public Test(String testId, Response response, List<Rule> vadilationRules) {
		this.testId = testId;
		this.response = response;
		this.vadilationRules = vadilationRules;
	}

	private String testId;

	private Response response;

	private List<Rule> vadilationRules;

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public List<Rule> getVadilationRules() {
		return vadilationRules;
	}

	public void setVadilationRules(List<Rule> vadilationRules) {
		this.vadilationRules = vadilationRules;
	}

	public static TestBuilder builder() {
		return new TestBuilder();
	}

	public static class TestBuilder {

		private String testId;

		private Response response;

		private List<Rule> vadilationRules;

		TestBuilder() {
		}

		public TestBuilder testId(String testId) {
			this.testId = testId;
			return this;
		}

		public TestBuilder response(Response response) {
			this.response = response;
			return this;
		}

		public TestBuilder vadilationRules(List<Rule> vadilationRules) {
			this.vadilationRules = vadilationRules;
			return this;
		}

		public Test build() {
			return new Test(testId, response, vadilationRules);

		}

		@java.lang.Override
		public String toString() {
			return "Test.TestBuilder(testId = " + this.testId + ", response = " + this.response + ", vadilationRules = "
					+ this.vadilationRules + ")";
		}

	}

}