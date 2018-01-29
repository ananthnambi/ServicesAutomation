package com.test.services.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.test.services.model.service.Response;

@SuppressWarnings({ "resource", "rawtypes", "unchecked", "unused" })
public class Test {

	public static void main(String[] args) throws IOException, ParseException {
		
		JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader("sample.json"));
		
		JSONArray array = (JSONArray) obj.get("items");
		
		JSONObject a = ((JSONObject)((JSONArray) ((JSONObject)array.get(0)).get("facilities")).get(0));
		
		String facilityId = (String) a.get("facilityId");
		
		String sourceFacilityType = (String) a.get("sourceFacilityType");
		
		String sourceFacilityId = (String) a.get("sourceFacilityId");
		
		JSONObject transportationLegs = (JSONObject) a.get("transportationLegs");
		
		JSONObject a1 = (JSONObject) transportationLegs.get("1");
		
		JSONObject a2 = (JSONObject) transportationLegs.get("2");
		
		String shippingFacilityId1 = (String) a1.get("shippingFacilityId");
		
		String receivingFacilityId1 =(String) a1.get("receivingFacilityId");
		
		String shippingFacilityId2 = (String) a2.get("shippingFacilityId");
		
		String receivingFacilityId2 =(String) a2.get("receivingFacilityId");
		
		
	}

	static void test2() throws IOException, ParseException {

		String json = "{\"hello\":\"hi\", \"int\":5, \"obj\":{\"inner\":\"yes\"}, \"array\":[{\"gala\":\"sad\"},{\"gala\":\"das\"}]}";

		Object d = Arrays.asList(new Boolean[] { false, false });

		JSONObject obj = (JSONObject) new JSONParser().parse(new StringReader(json));

		JSONArray array = (JSONArray) obj.get("array");

		Object in = true;

		System.out.println(((List) d).contains(in));

		String jsonPath = "$.asd.ger.gre";
		System.out.println(jsonPath + " : " + jsonPath.lastIndexOf("."));

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		String date = format.format(new Date());

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 1);

		System.out.println(date);

		System.out.println(format.format(c.getTime()));

		String trial = "aa&&bb||cc";

		String test[] = trial.split("&&|\\|\\|");

		System.out.println(Arrays.asList(test));

		String sym[] = trial.split("[a-zA-Z0-9]+");

		System.out.println(new ArrayList<String>(Arrays.asList(sym)).remove(""));

		System.out.println(sym.length);

		System.out.println("200".equals(Integer.toString(200)));

		// System.out.println("response "+JsonRetriever.get(json,
		// "array[2].gala"));

		List<String> sa = array;

		System.out.println(sa);

		List<String> st = (List<String>) null;
		System.out.println("st");

		String testing = "UTC12AER";

		if (testing.matches("TC.*")) {
			System.out.println(testing);
		} else {
			System.out.println("nothing");
		}

		int i = 0;
		String S = null;
		test();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		System.out.println("Day : " + cal.get(Calendar.DAY_OF_WEEK));

		System.out.println(ServicesUtility
				.ObjectToString(Response.builder().request("Hi").response("Hello").responseCode(200).build()));

		List<String> list = new ArrayList<String>();
		list.add("asdsadsa");

		System.out.println("list : " + list);

		list.addAll(list);
		System.out.println(25 % 25);

	}

	static void test() {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		String[] unsorted = new String[n];
		List<BigInteger> list = new ArrayList<BigInteger>();
		for (int unsorted_i = 0; unsorted_i < n; unsorted_i++) {
			unsorted[unsorted_i] = in.next();
			list.add(new BigInteger(unsorted[unsorted_i]));
		}
		Collections.sort(list);
		for (BigInteger bi : list) {
			System.out.println(bi);
		}
	}

	static void test1() {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		String[] unsorted = new String[n];
		List<String> list = new ArrayList<String>();
		int maxLength = 0;
		for (int unsorted_i = 0; unsorted_i < n; unsorted_i++) {
			unsorted[unsorted_i] = in.next();
			if (unsorted[unsorted_i].length() > maxLength) {
				maxLength = unsorted[unsorted_i].length();
			}
		}
		for (int unsorted_i = 0; unsorted_i < n; unsorted_i++) {
			String string = unsorted[unsorted_i];
			if (unsorted[unsorted_i].length() < maxLength) {
				int diff = maxLength - unsorted[unsorted_i].length();
				for (int i = 0; i < diff; i++) {
					string = "0" + string;
				}
			}
			list.add(string);
		}
		Collections.sort(list);
		for (String s : list) {
			System.out.println(s.replaceFirst("^0+(?!$)", ""));
		}

	}

	static void bucketSort() {

		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		String[] unsorted = new String[n];
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		for (int unsorted_i = 0; unsorted_i < n; unsorted_i++) {
			unsorted[unsorted_i] = in.next();
			List<String> list = map.get(unsorted[unsorted_i].length());
			if (list != null) {
				list.add(unsorted[unsorted_i]);
			} else {
				list = new ArrayList<String>();
				list.add(unsorted[unsorted_i]);
				map.put(unsorted[unsorted_i].length(), list);
			}
		}
		SortedSet<Integer> keys = new TreeSet<Integer>(map.keySet());
		for (Integer key : keys) {
			List<String> value = map.get(key);
			Collections.sort(value);
			for (String s : value) {
				System.out.println(s);
			}
		}

	}

}