package com.test.services.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.test.services.callback.Callback;
import com.test.services.model.mongo.Rule;
import com.test.services.model.service.Response;
import com.test.services.util.JsonRetriever;
import com.test.services.util.ServicesConstants;
import com.test.services.util.ServicesUtility;
import com.test.services.webservice.client.HttpClient;

/**
 * @author ananthnambi@gmail.com
 *
 */
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RuleEngine {

	/**
	 * LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleEngine.class);

	/**
	 * 
	 */
	@Autowired
	private HttpClient httpClient;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * @param rule
	 * @param data
	 * @return
	 */
	public String process(Rule rule, Object data) {

		LOGGER.info("Processing rule : " + rule);

		try {

			switch (rule.getDataType()) {

			case ServicesConstants.RULE_TYPE_JSON:

				return processJSON(rule, data);

			case ServicesConstants.RULE_TYPE_QUERY:

				return processQueryString(rule, (String) data);

			case ServicesConstants.RULE_TYPE_META:

				return processMetaData(rule, (Response) data);

			default:

				throw new RuntimeException("Invalid rule type!!!");

			}

		} catch (Exception ex) {

			LOGGER.error("Excpetion in RuleEngine", ex);

		}

		return ServicesConstants.RULE_FAIL;

	}

	/**
	 * @param rule
	 * @param data
	 * @return
	 */
	private String processQueryString(Rule rule, String data) {

		try {

			if (rule.getCallback() != null) {

				return callback(rule.getCallback(), data);

			} else if (rule.getDataSource() != null) {

				// TODO

			} else if (rule.getExternalSource() != null) {

				return callToExternalSource(rule.getExternalSource(), data);

			} else if (rule.getField() != null) {

				// map of parameter name, value
				Map<String, String> map = getQueryMap(data);

				if (ServicesUtility.isMultiConditional(rule.getField())) {

					return handleMultipleConditions(map, rule.getField(), rule.getCondition(),
							(String) rule.getValue());

				} else {

					return validateCondition(rule.getCondition(), rule.getValue(), map.get(rule.getField()));

				}

			}

		} catch (Exception ex) {

			LOGGER.error(" Excpetion in RuleEngine", ex);

		}

		return ServicesConstants.RULE_FAIL;

	}

	/**
	 * @param query
	 * @return
	 */
	private Map<String, String> getQueryMap(String query) {

		Map<String, String> map = new HashMap<String, String>();

		try {
			String[] params = query.split("&");

			for (String param : params) {

				String[] p = param.split("=");

				String name = p[0];

				if (p.length > 1) {

					String value = p[1];

					map.put(name, value);

				}

			}

		} catch (Exception ex) {

			LOGGER.error("Excpetion in RuleEngine", ex);

		}

		return map;
	}

	/**
	 * @param rule
	 * @param data
	 * @return
	 */
	private String processMetaData(Rule rule, Response data) {

		if (rule.getField().equals("responseCode")) {

			if (rule.getValue().equals(Integer.toString(data.getResponseCode())))
				return ServicesConstants.RULE_PASS;

		}

		return ServicesConstants.RULE_FAIL;
	}

	/**
	 * @param rule
	 * @param data
	 * @return
	 */
	private String processJSON(Rule rule, Object obj) {

		try {

			JSONObject data = null;
			Response response = null;

			if (obj instanceof Response)
				response = (Response) obj;
			else
				data = (JSONObject) obj;

			if (rule.getCallback() != null) {

				return callback(rule.getCallback(), (data != null) ? data : response);

			} else if (rule.getDataSource() != null) {

				// TODO

			} else if (rule.getExternalSource() != null) {

				return callToExternalSource(rule.getExternalSource(), (data != null) ? data : response);

			} else if (rule.getField() != null) {

				if (response != null)
					data = ServicesUtility.stringToJSON(response.getResponse());

				if (ServicesUtility.isMultiConditional(rule.getField())) {

					return handleMultipleConditions(data.toJSONString(), rule.getField(), rule.getCondition(),
							(String) rule.getValue());

				} else {

					Object actualValue = JsonRetriever.get(data.toJSONString(), rule.getField());

					return validateCondition(rule.getCondition(), rule.getValue(), actualValue);

				}

			}

		} catch (Exception ex) {

			LOGGER.error(" Excpetion in RuleEngine", ex);

		}

		return ServicesConstants.RULE_FAIL;

	}

	/**
	 * @param data
	 * @param fieldString
	 * @param conditionString
	 * @param valueString
	 * @return
	 */
	private String handleMultipleConditions(Object data, String fieldString, String conditionString,
			String valueString) {

		String fields[] = fieldString.split("&&|\\|\\|");

		String conditions[] = conditionString.split(",");

		String values[] = valueString.split(",");

		if (fields.length != conditions.length)
			throw new RuntimeException("Count of field and conditions aren't equal");

		if (fieldString.contains("&&")) {

			for (int i = 0; i < fields.length; i++) {

				// checking for map for query strings
				Object actualValue = (data instanceof Map<?, ?>) ? ((Map) data).get(fields[i].trim())
						: JsonRetriever.get((String) data, fields[i].trim());

				if (!validateCondition(conditions[i].trim(), values[i].trim(), actualValue)
						.equals(ServicesConstants.RULE_PASS))
					return ServicesConstants.RULE_FAIL;

			}

			return ServicesConstants.RULE_PASS;

		} else {

			for (int i = 0; i < fields.length; i++) {

				// checking for map for query strings
				Object actualValue = (data instanceof Map<?, ?>) ? ((Map) data).get(fields[i].trim())
						: JsonRetriever.get((String) data, fields[i].trim());

				if (validateCondition(conditions[i].trim(), values[i].trim(), actualValue)
						.equals(ServicesConstants.RULE_PASS))
					return ServicesConstants.RULE_PASS;

			}

			return ServicesConstants.RULE_FAIL;

		}

	}

	/**
	 * @param callback
	 * @param data
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private String callback(String callback, Object data)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException {

		String[] components = callback.split(ServicesConstants.DOT);

		String className = components[0];

		String methodName = components[1];

		Callback targetObject = (Callback) applicationContext.getBean(className);

		Class<Callback> classObj = (Class<Callback>) targetObject.getClass();

		Method method = classObj.getMethod(methodName, Object.class);

		return (String) method.invoke(targetObject, data);

	}

	/**
	 * @param externalSource
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private String callToExternalSource(String externalSource, Object data) throws Exception {

		Response response = httpClient.send(externalSource, ServicesConstants.REQUEST_METHOD_POST,
				ServicesConstants.CONTENT_TYPE_JSON, null, ServicesUtility.ObjectToString(data), null, false);

		if (response != null && response.getResponse() != null && response.getResponse().equals("true"))
			return ServicesConstants.RULE_PASS;

		return ServicesConstants.RULE_FAIL;

	}

	/**
	 * @param condition
	 * @param expectedValue
	 * @param actualValue
	 * @return
	 */
	private String validateCondition(String condition, Object expectedValue, Object actualValue) {

		switch (condition) {

		case ServicesConstants.CONDITION_EQUAL_TO:

			if (actualValue instanceof String) {

				if (actualValue.equals(expectedValue))
					return ServicesConstants.RULE_PASS;

			} else if (actualValue instanceof Integer) {

				if (actualValue.equals((int) expectedValue))
					return ServicesConstants.RULE_PASS;

			} else if (actualValue instanceof Double) {

				if (actualValue.equals(new Double((Integer) expectedValue)))
					return ServicesConstants.RULE_PASS;

			} else if (actualValue instanceof Boolean) {

				if (actualValue.equals(expectedValue))
					return ServicesConstants.RULE_PASS;

			}

			return ServicesConstants.RULE_FAIL;

		case ServicesConstants.CONDITION_NOT_EQUAL_TO:

			if (actualValue instanceof String) {

				if (actualValue.equals(expectedValue))
					return ServicesConstants.RULE_FAIL;

			} else if (actualValue instanceof Integer) {

				if (actualValue.equals((int) expectedValue))
					return ServicesConstants.RULE_FAIL;

			} else if (actualValue instanceof Double) {

				if (actualValue.equals(new Double((Integer) expectedValue)))
					return ServicesConstants.RULE_FAIL;

			} else if (actualValue instanceof Boolean) {

				if (actualValue.equals(expectedValue))
					return ServicesConstants.RULE_FAIL;

			}

			return ServicesConstants.RULE_PASS;

		case ServicesConstants.CONDITION_IS_EMPTY:

			if (actualValue instanceof String)
				return ((String) actualValue).isEmpty() ? ServicesConstants.RULE_PASS : ServicesConstants.RULE_FAIL;

			else if (actualValue instanceof List)
				return ((List) actualValue).isEmpty() ? ServicesConstants.RULE_PASS : ServicesConstants.RULE_FAIL;

			return ServicesConstants.RULE_FAIL;

		case ServicesConstants.CONDITION_IS_NOT_EMPTY:

			if (actualValue instanceof String)
				return ((String) actualValue).isEmpty() ? ServicesConstants.RULE_PASS : ServicesConstants.RULE_FAIL;

			else if (actualValue instanceof List)
				return ((List) actualValue).isEmpty() ? ServicesConstants.RULE_PASS : ServicesConstants.RULE_FAIL;

			return ServicesConstants.RULE_PASS;

		case ServicesConstants.CONDITION_IS_NULL:

			return actualValue == null ? ServicesConstants.RULE_PASS : ServicesConstants.RULE_FAIL;

		case ServicesConstants.CONDITION_IS_NOT_NULL:

			return actualValue != null ? ServicesConstants.RULE_PASS : ServicesConstants.RULE_FAIL;

		case ServicesConstants.CONDITION_CONTAINS:

			if (actualValue instanceof String)
				return ((String) actualValue).contains((String) expectedValue) ? ServicesConstants.RULE_PASS
						: ServicesConstants.RULE_FAIL;

			else if (actualValue instanceof List) {

				List listValue = (List) actualValue;

				if (listValue.isEmpty())
					return ServicesConstants.RULE_FAIL;

				Object indexZero = listValue.get(0);

				if (indexZero instanceof Integer)
					return listValue.contains((Integer) expectedValue) ? ServicesConstants.RULE_PASS
							: ServicesConstants.RULE_FAIL;
				else if (indexZero instanceof Double)
					return listValue.contains(new Double((Integer) expectedValue)) ? ServicesConstants.RULE_PASS
							: ServicesConstants.RULE_FAIL;
				else
					return listValue.contains(expectedValue) ? ServicesConstants.RULE_PASS
							: ServicesConstants.RULE_FAIL;

			}

			return ServicesConstants.RULE_FAIL;

		case ServicesConstants.CONDITION_IN:

			if (expectedValue instanceof List) {

				return ((List) expectedValue).contains(actualValue) ? ServicesConstants.RULE_PASS
						: ServicesConstants.RULE_FAIL;

			}

			return ServicesConstants.RULE_FAIL;

		case ServicesConstants.CONDITION_NOT_IN:

			if (expectedValue instanceof List) {

				return !((List) expectedValue).contains(actualValue) ? ServicesConstants.RULE_PASS
						: ServicesConstants.RULE_FAIL;

			}

			return ServicesConstants.RULE_FAIL;

		default:

			throw new RuntimeException("Invalid condition !");

		}
	}

}