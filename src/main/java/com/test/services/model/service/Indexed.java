package com.test.services.model.service;

import org.json.simple.JSONArray;

public class Indexed {

	public Indexed(String jsonPath, JSONArray values) {
		super();
		this.jsonPath = jsonPath;
		this.values = values;
	}

	private String jsonPath;

	private JSONArray values;

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

	public JSONArray getValues() {
		return values;
	}

	public void setValues(JSONArray values) {
		this.values = values;
	}

	public static IndexedBuilder builder() {
		return new IndexedBuilder();
	}

	public static class IndexedBuilder {

		private String jsonPath;

		private JSONArray values;

		IndexedBuilder() {
		}

		public IndexedBuilder jsonPath(String jsonPath) {
			this.jsonPath = jsonPath;
			return this;
		}

		public IndexedBuilder values(JSONArray values) {
			this.values = values;
			return this;
		}

		public Indexed build() {
			return new Indexed(jsonPath, values);

		}

		@java.lang.Override
		public String toString() {
			return "Indexed.IndexedBuilder(jsonPath = " + this.jsonPath + ", values = " + this.values + ")";
		}

	}

}