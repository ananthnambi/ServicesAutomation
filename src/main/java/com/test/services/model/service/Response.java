package com.test.services.model.service;

public class Response {

	public Response(String request, String response, String contentType, int responseCode) {
		super();
		this.request = request;
		this.response = response;
		this.contentType = contentType;
		this.responseCode = responseCode;
	}

	private String request;

	private String response;

	private String contentType;

	private int responseCode;

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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public static ResponseBuilder builder() {
		return new ResponseBuilder();
	}

	public static class ResponseBuilder {

		private String request;

		private String response;

		private String contentType;

		private int responseCode;

		ResponseBuilder() {
		}

		public ResponseBuilder request(String request) {
			this.request = request;
			return this;
		}

		public ResponseBuilder response(String response) {
			this.response = response;
			return this;
		}

		public ResponseBuilder contentType(String contentType) {
			this.contentType = contentType;
			return this;
		}

		public ResponseBuilder responseCode(int responseCode) {
			this.responseCode = responseCode;
			return this;
		}

		public Response build() {
			return new Response(request, response, contentType, responseCode);

		}

		@java.lang.Override
		public String toString() {
			return "DetailedReport.DetailedReportBuilder(request = " + this.request + ", response = " + this.response
					+ ", contentType = " + this.contentType + ", responseCode = " + this.responseCode + ")";
		}

	}

}