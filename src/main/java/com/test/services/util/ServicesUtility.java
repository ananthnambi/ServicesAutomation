package com.test.services.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author ananthnambi@gmail.com
 *
 */
public class ServicesUtility {

	/**
	 * 
	 */
	private static final JSONParser parser = new JSONParser();

	/**
	 * GSON instance
	 */
	private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	/**
	 * 
	 */
	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

	/**
	 * @param data
	 * @return
	 */
	public static String ObjectToString(Object data) {

		if (data instanceof JSONObject) {

			return ((JSONObject) data).toJSONString();

		} else {

			return gson.toJson(data);

		}

	}

	/**
	 * @param input
	 * @return
	 * @throws ParseException
	 */
	public static JSONObject stringToJSON(String input) throws ParseException {

		return (JSONObject) parser.parse(input);

	}

	/**
	 * @param input
	 * @return
	 * @throws ParseException
	 */
	public static Object stringToObject(String input) throws ParseException {

		return parser.parse(input);

	}

	/**
	 * @param toCheck
	 * @return
	 */
	public static boolean isMultiConditional(String toCheck) {

		return (toCheck.contains(ServicesConstants.CONDITIONAL_AND)
				|| toCheck.contains(ServicesConstants.CONDITIONAL_OR));

	}

	/**
	 * @param timestamp
	 * @return
	 */
	public static String getStringDateTime(long timestamp) {

		return dateTimeFormat.format(new Date(timestamp));

	}

	public static String convertJsonToString(Object jsonObject) {

		return gson.toJson(jsonObject);

	}

	public static Object convertStringToJson(String jsonString, Class<?> jsonType) {

		return gson.fromJson(jsonString, jsonType);

	}

}