package com.test.services.model.mongo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "report")
public class Report {

	private String reportId;

	private String testId;

	private String status;

	private String request;

	private int responseCode;

	private String response;

	private List<String> failedRules;

	private long timestamp;

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public List<String> getFailedRules() {
		return failedRules;
	}

	public void setFailedRules(List<String> failedRules) {
		this.failedRules = failedRules;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}