package com.test.services.callback;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.test.services.dao.custom.CustomDAO;
import com.test.services.util.ConfigService;
import com.test.services.webservice.client.HttpClient;

/**
 * @author ananthnambi@gmail.com
 *
 *         Callback is a class which will be invoked by the RuleEngine when a
 *         particular class name and method name is given as a part of the rule.
 *         The parameters for these methods should be
 *         <b>org.json.simple.JSONObject</b> for <b>request rule</b> and
 *         <b>com.shc.oms.test.services.model.service.Response</b> for a
 *         <b>response rule</b>.
 * 
 */
public abstract class Callback {

	/**
	 * LOGGER.
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(Callback.class);

	/**
	 * To access a data object.
	 */
	@Autowired
	private CustomDAO dataAccessObject;

	/**
	 * ConfigService to access all config data.
	 */
	@Autowired
	private ConfigService configService;

	/**
	 * To hit other services and get data.
	 */
	@Autowired
	private HttpClient httpClient;

	/**
	 * The default datasource of the implementing service.
	 */
	private String defaultDatasource;

	/**
	 * Service name of child class to fetch default datasource.
	 */
	private String callbackName;

	/**
	 * Callback is a class which will be invoked by the RuleEngine when a
	 * particular class name and method name is given as a part of the rule. The
	 * parameters for these methods should be <b>org.json.simple.JSONObject</b>
	 * for <b>request rule</b> and
	 * <b>com.shc.oms.test.services.model.service.Response</b> for a <b>response
	 * rule</b>.
	 * 
	 * @param callbackName
	 */
	protected Callback(String callbackName) {

		this.callbackName = callbackName;

	}

	// Getter methods for instance variables
	protected CustomDAO getDataAccessObject() {
		return dataAccessObject;
	}

	protected ConfigService getConfigService() {
		return configService;
	}

	protected HttpClient getHttpClient() {
		return httpClient;
	}

	protected String getDefaultDatasource() {
		return defaultDatasource;
	}

	/**
	 * Called for every constructor class after initialization.
	 */
	@PostConstruct
	private void init() {

		if (callbackName != null)
			defaultDatasource = configService.getDefaultDatasource(callbackName);

		LOGGER.info("Started callback component : " + callbackName);

	}

}