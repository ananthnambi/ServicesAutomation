package com.test.services.util;

/**
 *
 * This class contains all the constants used in Services Automation.
 *
 */
public class ServicesConstants {

	// use constant for report id generation
	public final static String REPORT_ID_USE = "report";

	// beans
	public final static String BEAN_REQUEST_PROCESSOR = "requestProcessor";

	// rule constants
	public final static String CONDITION_EQUAL_TO = "equalTo";
	public final static String CONDITION_NOT_EQUAL_TO = "notEqualTo";
	public final static String CONDITION_IS_EMPTY = "isEmpty";
	public final static String CONDITION_IS_NOT_EMPTY = "isNotEmpty";
	public final static String CONDITION_IS_NULL = "isNull";
	public final static String CONDITION_IS_NOT_NULL = "isNotNull";
	public final static String CONDITION_CONTAINS = "contains";
	public final static String CONDITION_IN = "in";
	public final static String CONDITION_NOT_IN = "notIn";

	// mongo fields
	public final static String FIELD_TIMESTAMP = "timestamp";
	public final static String FIELD_ZIPCODE = "zipCode";

	// boolean
	public final static String BOOLEAN_TRUE = "true";
	public final static String BOOLEAN_FALSE = "false";

	// utilities
	public final static String UTF8 = "UTF-8";
	public final static String LONG_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	// symbols
	public final static String DOT = "\\.";
	public final static String LIKE_MANY = "*";
	public final static String UNDERSCORE = "_";
	public final static String COMMA = ",";
	public final static String SEMI_COLON = ";";
	public final static String REGEX_SPACE = "\\s+";
	public final static String BLANK = "";
	public final static String HYPHEN = "-";
	public final static String COLON = ":";
	public final static String PIPING = "|";
	public final static String QUESTION_MARK = "?";
	public final static String EQUALS = "=";
	public final static String SLASH = "/";
	public final static String GREATER_THAN = ">";
	public final static String AMPERSAND = "&";

	// conditional
	public final static String CONDITIONAL_AND = "&&";
	public final static String CONDITIONAL_OR = "||";

	// rule constants
	public final static String RULE_TYPE_JSON = "json";
	public final static String RULE_TYPE_META = "meta";
	public final static String RULE_TYPE_QUERY = "query";

	// constants for HTTP client
	public final static String HTTP_REQUEST_ACCEPT = "Accept";
	public final static String HTTP_REQUEST_AUTHORIZATION = "Authorization";
	public final static String HTTP_METHOD_POST = "POST";
	public final static String HTTP_METHOD_GET = "GET";
	public final static String HTTP_REQUEST_CONTENT_TYPE = "Content-Type";
	public final static String HTTP_METHOD_PUT = "PUT";
	public final static String HTTP_TYPE_JSON = "application/json";

	// audit status
	public final static String RULE_PASS = "PASS";
	public final static String RULE_FAIL = "FAIL";
	public final static String TEST_CASE_PASS = "PASS";
	public final static String TEST_CASE_FAIL = "FAIL";
	public final static String TEST_CASE_INPROGRESS = "INPROGRESS";
	public final static String TEST_CASE_COMPLETED = "COMPLETED";

	// http constants
	public final static String REQUEST_METHOD_POST = "POST";
	public final static String CONTENT_TYPE_JSON = "application/json";
	public final static String RETURN_TYPE_JSON = "application/json";

}