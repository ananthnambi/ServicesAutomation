package com.test.services.model.json;

import java.util.List;

public class DetailedReport {

	public DetailedReport(String request, String response, String responseCode, List<String> failedRules,
			String dateTime) {
		this.request = request;
		this.response = response;
		this.responseCode = responseCode;
		this.failedRules = failedRules;
		this.dateTime = dateTime;
	}

	private String request;

	private String response;

	private String responseCode;

	private List<String> failedRules;

	private String dateTime;

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public List<String> getFailedRules() {
		return failedRules;
	}

	public void setFailedRules(List<String> failedRules) {
		this.failedRules = failedRules;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public static DetailedReportBuilder builder() {
		return new DetailedReportBuilder();
	}

	public static class DetailedReportBuilder {

		private String request;

		private String response;

		private String responseCode;

		private List<String> failedRules;

		private String dateTime;

		DetailedReportBuilder() {
		}

		public DetailedReportBuilder request(String request) {
			this.request = request;
			return this;
		}

		public DetailedReportBuilder response(String response) {
			this.response = response;
			return this;
		}

		public DetailedReportBuilder responseCode(String responseCode) {
			this.responseCode = responseCode;
			return this;
		}

		public DetailedReportBuilder failedRules(List<String> failedRules) {
			this.failedRules = failedRules;
			return this;
		}

		public DetailedReportBuilder dateTime(String dateTime) {
			this.dateTime = dateTime;
			return this;
		}

		public DetailedReport build() {
			return new DetailedReport(request, response, responseCode, failedRules, dateTime);

		}

		@java.lang.Override
		public String toString() {
			return "DetailedReport.DetailedReportBuilder(request = " + this.request + ", response = " + this.response
					+ ", responseCode = " + this.responseCode + ", failedRules = " + this.failedRules + ", dateTime = "
					+ this.dateTime + ")";
		}

	}

}