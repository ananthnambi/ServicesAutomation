package com.test.services.model.json;

public class TestReport {

	public TestReport(String testId, String description, int noOfValidations, int passCount, double passPercentage,
			int failCount, double failPercentage) {
		this.testId = testId;
		this.description = description;
		this.noOfValidations = noOfValidations;
		this.passCount = passCount;
		this.passPercentage = passPercentage;
		this.failCount = failCount;
		this.failPercentage = failPercentage;
	}

	private String testId;

	private String description;

	private int noOfValidations;

	private int passCount;

	private double passPercentage;

	private int failCount;

	private double failPercentage;

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNoOfValidations() {
		return noOfValidations;
	}

	public void setNoOfValidations(int noOfValidations) {
		this.noOfValidations = noOfValidations;
	}

	public int getPassCount() {
		return passCount;
	}

	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}

	public double getPassPercentage() {
		return passPercentage;
	}

	public void setPassPercentage(double passPercentage) {
		this.passPercentage = passPercentage;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public double getFailPercentage() {
		return failPercentage;
	}

	public void setFailPercentage(double failPercentage) {
		this.failPercentage = failPercentage;
	}

	public static TestReportBuilder builder() {
		return new TestReportBuilder();
	}

	public static class TestReportBuilder {

		private String testId;

		private String description;

		private int noOfValidations;

		private int passCount;

		private double passPercentage;

		private int failCount;

		private double failPercentage;

		TestReportBuilder() {
		}

		public TestReportBuilder testId(String testId) {
			this.testId = testId;
			return this;
		}

		public TestReportBuilder description(String description) {
			this.description = description;
			return this;
		}

		public TestReportBuilder noOfValidations(int noOfValidations) {
			this.noOfValidations = noOfValidations;
			return this;
		}

		public TestReportBuilder passCount(int passCount) {
			this.passCount = passCount;
			return this;
		}

		public TestReportBuilder passPercentage(double passPercentage) {
			this.passPercentage = passPercentage;
			return this;
		}

		public TestReportBuilder failCount(int failCount) {
			this.failCount = failCount;
			return this;
		}

		public TestReportBuilder failPercentage(double failPercentage) {
			this.failPercentage = failPercentage;
			return this;
		}

		public TestReport build() {
			return new TestReport(testId, description, noOfValidations, passCount, passPercentage, failCount,
					failPercentage);

		}

		@java.lang.Override
		public String toString() {
			return "TestReport.TestReportBuilder(testId = " + this.testId + ", description = " + this.description
					+ ", noOfValidations = " + this.noOfValidations + ", passCount = " + this.passCount
					+ ", passPercentage = " + this.passPercentage + ", failCount = " + this.failCount
					+ ", failPercentage = " + this.failPercentage + ")";
		}

	}

}