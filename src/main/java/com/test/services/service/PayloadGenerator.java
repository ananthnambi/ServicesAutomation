package com.test.services.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;
import com.test.services.exception.InvalidIndexingException;
import com.test.services.model.service.Indexed;
import com.test.services.util.ServicesUtility;

@Service
public class PayloadGenerator {

	/**
	 * GSON instance
	 */
	private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	/**
	 * 
	 */
	private static final JSONParser parser = new JSONParser();

	public static void main(String args[]) throws FileNotFoundException, IOException, ParseException {

		String json = ((JSONObject) parser.parse(new FileReader("ServicesSample.json"))).toJSONString();

		PayloadGenerator pg = new PayloadGenerator();

		List<Object> list = pg.service(json, "JSON", null);
		System.out.println(list);
		System.out.println(list.size());

		list = pg.removeNullFields(list);
		System.out.println(list);
		System.out.println(list.size());

	}

	/**
	 * @param schemaString
	 * @param schemaType
	 * @return
	 */
	public List<Object> service(String schemaString, String schemaType, String separator) {

		List<Object> finalList = new ArrayList<Object>();

		try {

			switch (schemaType) {

			case "JSON":

				processJSON(finalList, schemaString);

				break;

			case "QueryString":

				processQueryString(finalList, schemaString, separator);

				break;

			default:

				throw new RuntimeException("Content type is not supported !!");

			}

		} catch (Exception ex) {

			ex.printStackTrace();

		}

		return finalList;

	}

	/**
	 * @param finalList
	 * @param schemaString
	 * @throws ParseException
	 * @throws InvalidIndexingException
	 */
	private void processJSON(List<Object> finalList, String schemaString)
			throws ParseException, InvalidIndexingException {

		List<Indexed> indexedList = new ArrayList<Indexed>();

		JSONObject schema = ServicesUtility.stringToJSON(schemaString);

		processObject(finalList, indexedList, schema, "$");

		if (!indexedList.isEmpty())
			processIndexedEntries(finalList, indexedList);

	}

	/**
	 * @param finalList
	 * @param schemaString
	 * @throws ParseException
	 * @throws InvalidIndexingException
	 */
	private void processQueryString(List<Object> finalList, String schemaString, String separator)
			throws ParseException, InvalidIndexingException {

		if (separator == null || separator.isEmpty())
			separator = "=";

		processJSON(finalList, schemaString);

		// To remove unwanted fields from the payload
		finalList = removeNullFields(finalList);

		List<Object> tempList = new ArrayList<Object>();

		for (Object obj : finalList) {

			tempList.add(jsonToQueryString((JSONObject) obj, separator));

		}

		finalList.clear();

		finalList.addAll(tempList);

	}

	/**
	 * @param json
	 * @param separator
	 * @return
	 */
	private String jsonToQueryString(JSONObject json, String separator) {

		String query = "";

		for (Object value : json.entrySet()) {

			@SuppressWarnings("unchecked")
			Entry<String, Object> entry = (Entry<String, Object>) value;

			query = query + entry.getKey() + separator + entry.getValue() + "&";

		}

		return query.substring(0, query.length() - 1);

	}

	/**
	 * @param finalList
	 * @param indexedList
	 * @param object
	 * @param jsonPath
	 * @throws ParseException
	 * @throws InvalidIndexingException
	 */
	private void processObject(List<Object> finalList, List<Indexed> indexedList, JSONObject object, String jsonPath)
			throws ParseException, InvalidIndexingException {

		if (finalList.isEmpty()) {

			finalList.add(new JSONObject());

		} else {

			for (int i = 0; i < finalList.size(); i++) {

				if (jsonPath.lastIndexOf("]") == (jsonPath.length() - 1)) {

					List<Object> toSave = new ArrayList<Object>();
					toSave.add(new Object());

					finalList.set(i, (JSONObject) setValueToPath((JSONObject) finalList.get(i),
							jsonPath.substring(0, jsonPath.lastIndexOf("[")), toSave));

				} else {

					finalList.set(i,
							(JSONObject) setValueToPath((JSONObject) finalList.get(i), jsonPath, new Object()));

				}

			}
		}

		JSONObject properties = (JSONObject) object.get("properties");

		@SuppressWarnings("unchecked")
		Set<Entry<String, JSONObject>> entrySet = properties.entrySet();

		for (Entry<String, JSONObject> entry : entrySet) {

			String name = entry.getKey();

			JSONObject attribute = entry.getValue();

			String type = (String) attribute.get("type");

			switch (type) {

			case "object":
				processObject(finalList, indexedList, attribute, jsonPath + "." + name);
				continue;

			case "array": {
				processArray(finalList, indexedList, attribute, jsonPath + "." + name);
				continue;
			}

			default:
				processProperty(finalList, indexedList, attribute, jsonPath + "." + name);
				continue;

			}

		}

	}

	/**
	 * @param finalList
	 * @param indexedList
	 * @param array
	 * @param jsonPath
	 * @throws ParseException
	 * @throws InvalidIndexingException
	 */
	private void processArray(List<Object> finalList, List<Indexed> indexedList, JSONObject array, String jsonPath)
			throws ParseException, InvalidIndexingException {

		JSONObject items = (JSONObject) array.get("items");

		String type = (String) items.get("type");

		for (int i = 0; i < finalList.size(); i++) {

			finalList.set(i, (JSONObject) setValueToPath((JSONObject) finalList.get(i), jsonPath, new ArrayList<>()));

		}

		switch (type) {

		case "object": {
			processObject(finalList, indexedList, items, jsonPath + "[0]");
			return;
		}

		default:
			processProperty(finalList, indexedList, items, jsonPath);
			return;

		}

	}

	/**
	 * @param finalList
	 * @param indexedList
	 * @param schema
	 * @param jsonPath
	 * @throws ParseException
	 * @throws InvalidIndexingException
	 */
	@SuppressWarnings("unchecked")
	private void processProperty(List<Object> finalList, List<Indexed> indexedList, JSONObject schema, String jsonPath)
			throws ParseException, InvalidIndexingException {

		String use = (String) schema.get("use");

		String type = (String) schema.get("type");

		if (use.equals("single")) {

			for (int i = 0; i < finalList.size(); i++) {

				finalList.set(i,
						(JSONObject) setValueToPath((JSONObject) finalList.get(i), jsonPath, schema.get("value")));

			}

		} else if (use.equals("range")) {

			if (!type.equals("integer"))
				throw new RuntimeException("Invalid type for use !!!");

			String regex = "\\d+";

			Matcher matcher = Pattern.compile(regex).matcher((String) schema.get("value"));

			int min = -1;
			int max = -1;

			while (matcher.find()) {

				if (min == -1)
					min = Integer.parseInt(matcher.group(0));
				else
					max = Integer.parseInt(matcher.group(0));

			}

			if (max < min)
				throw new RuntimeException("Invalid range!!!");

			int noOfTests = max - (min - 1);

			List<Object> tempList = new ArrayList<Object>();

			for (Object obj : finalList) {

				int lastIndex = tempList.size();

				tempList.addAll(Collections.nCopies(noOfTests, obj));

				int valueToSet = min;

				for (int i = lastIndex; i < (lastIndex + noOfTests); i++) {

					tempList.set(i, (JSONObject) setValueToPath((JSONObject) tempList.get(i), jsonPath, valueToSet));

					valueToSet++;

				}

			}

			finalList.clear();

			finalList.addAll(tempList);

		} else if (use.equals("iterative")) {

			JSONArray array = (JSONArray) schema.get("value");

			int noOfTests = array.size();

			List<Object> tempList = new ArrayList<Object>();

			for (Object obj : finalList) {

				int lastIndex = tempList.size();

				tempList.addAll(Collections.nCopies(noOfTests, obj));

				int currentIndex = 0;

				for (int i = lastIndex; i < (lastIndex + noOfTests); i++) {

					tempList.set(i, (JSONObject) setValueToPath((JSONObject) tempList.get(i), jsonPath,
							array.get(currentIndex)));

					currentIndex++;

				}

			}

			finalList.clear();

			finalList.addAll(tempList);

		} else if (use.equals("arrayElement")) {

			JSONArray array = (JSONArray) schema.get("value");

			String arrayPath = getArrayPath(jsonPath);

			String currentNode = getCurrentNode(jsonPath);

			for (int i = 0; i < finalList.size(); i++) {

				JSONObject obj = (JSONObject) finalList.get(i);

				JSONArray toReplicate = (JSONArray) getArrayValue(obj, arrayPath);

				List<Object> tempList = new ArrayList<>();

				int prevSize = 0;

				for (Object toCopy : toReplicate) {

					prevSize = tempList.size();

					for (int k = 0; k < array.size(); k++) {

						tempList.add(ServicesUtility.stringToJSON(((JSONObject) toCopy).toJSONString()));

					}

					int arrayIndex = 0;

					for (int j = prevSize; j < (array.size() + prevSize); j++) {

						((JSONObject) tempList.get(j)).put(currentNode, array.get(arrayIndex));

						arrayIndex++;
					}

				}

				finalList.set(i, (JSONObject) setValueToPath((JSONObject) finalList.get(i), arrayPath, tempList));

			}

		} else if (use.equals("date")) {

			if (!type.equals("string"))
				throw new RuntimeException("Invalid type for use !!!");

			JSONArray array = (JSONArray) schema.get("value");

			String dateFormat = (String) schema.get("format");

			long range = (long) schema.get("range");

			SimpleDateFormat format = new SimpleDateFormat(dateFormat);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());

			if (range > -1) {

				for (int i = -1; i < range; i++) {

					if (i != -1) {

						calendar.add(Calendar.DATE, 1);

					}

					array.add(format.format(calendar.getTime()));

				}

			} else {

				for (int i = 1; i > range; i--) {

					if (i != 1) {

						calendar.add(Calendar.DATE, -1);

					}

					array.add(format.format(calendar.getTime()));

				}

			}

			int noOfTests = array.size();

			List<Object> tempList = new ArrayList<Object>();

			for (Object obj : finalList) {

				int lastIndex = tempList.size();

				tempList.addAll(Collections.nCopies(noOfTests, obj));

				int currentIndex = 0;

				for (int i = lastIndex; i < (lastIndex + noOfTests); i++) {

					tempList.set(i, (JSONObject) setValueToPath((JSONObject) tempList.get(i), jsonPath,
							array.get(currentIndex)));

					currentIndex++;

				}

			}

			finalList.clear();

			finalList.addAll(tempList);

		} else if (use.equals("indexed")) {

			JSONArray array = (JSONArray) schema.get("value");

			int noOfTests = array.size();

			if (indexedList.isEmpty()) {

				List<Object> tempList = new ArrayList<Object>();

				for (Object obj : finalList) {

					tempList.addAll(Collections.nCopies(noOfTests, obj));

				}

				finalList.clear();

				finalList.addAll(tempList);

				indexedList.add(Indexed.builder().jsonPath(jsonPath).values(array).build());

			} else {

				if (indexedList.get(0).getValues().size() == noOfTests) {

					indexedList.add(Indexed.builder().jsonPath(jsonPath).values(array).build());

				} else {

					throw new InvalidIndexingException(
							"Indexed properties are having unequal length - Check schema !!!");

				}

			}

		}

	}

	/**
	 * @param json
	 * @param jsonPath
	 * @return
	 * @throws ParseException
	 */
	private Object getArrayValue(JSONObject json, String jsonPath) throws ParseException {

		return ServicesUtility.stringToObject(JsonPath.parse(json.toJSONString()).read(jsonPath).toString());

	}

	/**
	 * @param json
	 * @param jsonPath
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	private Object setValueToPath(JSONObject json, String jsonPath, Object value) throws ParseException {

		int pos = jsonPath.lastIndexOf(".");

		String key = jsonPath.substring(pos + 1);

		jsonPath = jsonPath.substring(0, pos);

		return ServicesUtility.stringToJSON(JsonPath.parse(json.toJSONString()).put(jsonPath, key, value).jsonString());

	}

	/**
	 * @param jsonPath
	 * @return
	 */
	private String getArrayPath(String jsonPath) {

		return jsonPath.substring(0, jsonPath.lastIndexOf("["));

	}

	/**
	 * @param jsonPath
	 * @return
	 */
	private String getCurrentNode(String jsonPath) {

		return jsonPath.substring(jsonPath.lastIndexOf(".") + 1);

	}

	/**
	 * @param finalList
	 * @param indexedList
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	private void processIndexedEntries(List<Object> finalList, List<Indexed> indexedList) throws ParseException {

		for (int index = 0; index < finalList.size(); index++) {

			JSONObject jsonObj = (JSONObject) finalList.get(index);

			int currLoc = index % (indexedList.get(0).getValues().size());

			for (int param = 0; param < indexedList.size(); param++) {

				String jsonPath = indexedList.get(param).getJsonPath();

				Object value = indexedList.get(param).getValues().get(currLoc);

				if (!(value instanceof JSONArray)) {

					jsonObj = (JSONObject) setValueToPath(jsonObj, jsonPath, value);

				} else {

					// indexed array implementation

					JSONArray valueArray = (JSONArray) value;

					String arrayPath = getArrayPath(jsonPath);

					JSONObject targetObj = (JSONObject) ((JSONArray) getArrayValue(jsonObj, arrayPath)).get(0);

					JSONArray targetArray = new JSONArray();

					for (int arrayIndex = 0; arrayIndex < valueArray.size(); arrayIndex++) {

						JSONObject valueObj = new JSONObject(targetObj);

						valueObj.put(getCurrentNode(jsonPath), valueArray.get(arrayIndex));

						targetArray.add(valueObj);

					}

					jsonObj = (JSONObject) setValueToPath(jsonObj, arrayPath, targetArray);

				}

			}

			finalList.set(index, jsonObj);

		}

	}

	/**
	 * @param jsons
	 * @return
	 */
	private List<Object> removeNullFields(List<Object> jsons) {

		List<Object> result = new ArrayList<Object>();

		try {

			for (Object json : jsons) {

				result.add(((JSONObject) parser.parse(gson.toJson(json))));

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

}