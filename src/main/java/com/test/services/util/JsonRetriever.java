package com.test.services.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * This utility class can be used to fetch any value given the key path is
 * correct
 * 
 *
 */
public class JsonRetriever {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonRetriever.class);

	private static final String REGEX_JSON_ARRAY = "\\w*\\[\\d*]$";
	private static final String REGEX_JSON_ARRAY_INDEX = "\\[\\d*]$";

	/**
	 * Extract the value from JSON string using the path
	 * 
	 * @param jsonContent
	 * @param pathForExtraction
	 * @return value at pathForExtraction
	 */
	public static Object get(String jsonContent, String pathForExtraction) {
		Object jsonResult = null;
		JSONParser jsonparser = new JSONParser();
		try {
			jsonContent = jsonparser.parse(jsonContent).toString();
			Gson gson = new Gson();
			Object jsonObj = gson.fromJson(jsonContent, Object.class);
			String arrayJsonPath[] = pathForExtraction.split(Pattern.quote("."));
			jsonResult = iterate(jsonObj, pathForExtraction, arrayJsonPath.length - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}

	private static Object iterate(Object jsonObj, String jsonPath, int jsonSize) {
		String currentJsonPath = "";
		try {
			String arrayJsonPath[] = jsonPath.split(Pattern.quote("."));
			int actualLength = jsonPath.split(Pattern.quote(".")).length - 1;

			while (jsonSize >= 0) {
				currentJsonPath = arrayJsonPath[actualLength - jsonSize];
				if (!currentJsonPath.matches(REGEX_JSON_ARRAY)) {
					jsonObj = PropertyUtils.getProperty(jsonObj, currentJsonPath);
					jsonSize--;
					continue;
				}

				int index = 0;
				int indexValue = 0;
				Pattern pattern = Pattern.compile(REGEX_JSON_ARRAY_INDEX);
				Matcher matcher = pattern.matcher(currentJsonPath);
				if (matcher.find()) {
					indexValue = new Integer(currentJsonPath.substring(matcher.start() + 1, matcher.end() - 1))
							.intValue();
					currentJsonPath = currentJsonPath.substring(0, matcher.start());
				}
				jsonObj = PropertyUtils.getProperty(jsonObj, currentJsonPath);
				if (jsonObj instanceof List) {
					List<?> resultList = (ArrayList<?>) jsonObj;
					jsonObj = resultList.get(indexValue);
				} else if (jsonObj instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, Object> resultMap = (LinkedHashMap<String, Object>) jsonObj;
					for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
						if (indexValue == index) {
							Object value = entry.getValue();
							jsonObj = value;
							break;
						}
						index++;
					}
				}
				jsonSize--;
			}

		} catch (Exception e) {
			LOGGER.error("key: " + jsonPath + " not found");
			e.printStackTrace();
			jsonObj = null;
		}
		return jsonObj;
	}
}
