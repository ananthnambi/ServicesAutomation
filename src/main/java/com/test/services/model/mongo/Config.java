package com.test.services.model.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config")
public class Config {

	private String serviceName;

	private String datasource;

	private String name;

	private String value;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}