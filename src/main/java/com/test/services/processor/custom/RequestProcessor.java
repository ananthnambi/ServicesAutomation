package com.test.services.processor.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.test.services.model.mongo.Rule;
import com.test.services.model.mongo.Schema;
import com.test.services.model.mongo.Testcase;
import com.test.services.model.service.Response;
import com.test.services.model.service.Test;
import com.test.services.processor.Processor;
import com.test.services.repository.mongo.RulesRepository;
import com.test.services.repository.mongo.SchemaRepository;
import com.test.services.repository.mongo.TestcasesRepository;
import com.test.services.service.PayloadGenerator;
import com.test.services.service.ResponseValidator;
import com.test.services.service.RuleEngine;
import com.test.services.util.ServicesConstants;
import com.test.services.util.ServicesUtility;
import com.test.services.webservice.client.HttpClient;

/**
 * @author ananthnambi@gmail.com
 *
 */
@Component
@Scope("prototype")
public class RequestProcessor implements Processor {

	/**
	 * LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestProcessor.class);

	/**
	 * To generate payload for a given schema.
	 */
	@Autowired
	private PayloadGenerator payloadGenerator;

	/**
	 * To fetch service details for a given test to run.
	 */
	@Autowired
	private SchemaRepository schemaRepository;

	/**
	 * To fetch the rules for the test being run currently.
	 */
	@Autowired
	private RulesRepository rulesRepository;

	/**
	 * To fetch the test cases for the test being run currently.
	 */
	@Autowired
	private TestcasesRepository testcasesRepository;

	/**
	 * To validate the rules - used only for request rules.
	 */
	@Autowired
	private RuleEngine ruleEngine;

	/**
	 * To make http requests.
	 */
	@Autowired
	private HttpClient httpClient;

	/**
	 * 
	 */
	@Autowired
	private ResponseValidator responseValidator;

	/**
	 * 
	 */
	private String testSuite;

	/**
	 * 
	 */
	private String reportId;

	/**
	 * @param testSuite
	 * @param reportId
	 */
	public RequestProcessor(String testSuite, String reportId) {

		LOGGER.info("RequestProcessor invoked for " + testSuite + " and id " + reportId);

		this.testSuite = testSuite;
		this.reportId = reportId;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		if (testSuite != null && !testSuite.isEmpty()) {

			process();

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shc.oms.test.services.processor.Processor#process()
	 */
	@Override
	public void process() {

		try {

			Schema schema = schemaRepository.findOneByTestSuite(testSuite);

			List<Object> requestToSend = payloadGenerator.service(schema.getSchema(), schema.getSchemaType(),
					schema.getSeperator());

			List<Testcase> testcases = testcasesRepository.findByTestIdLike(testSuite);

			Map<String, List<Rule>> inputTestCaseMap = new HashMap<String, List<Rule>>();

			Map<String, List<Rule>> outputTestCaseMap = new HashMap<String, List<Rule>>();

			for (Testcase testcase : testcases) {

				if (schema.getSeperator() != null) {

					List<Rule> inputRules = rulesRepository.findByRuleIdIn(testcase.getInputRules());

					inputRules.stream().forEach(rule -> rule.setSeparator(schema.getSeperator()));

					inputTestCaseMap.put(testcase.getTestId(), inputRules);

				} else {

					inputTestCaseMap.put(testcase.getTestId(),
							rulesRepository.findByRuleIdIn(testcase.getInputRules()));

				}

				outputTestCaseMap.put(testcase.getTestId(), rulesRepository.findByRuleIdIn(testcase.getOutputRules()));

			}

			List<Test> testsToValidate = new ArrayList<Test>();

			for (Object body : requestToSend) {

				for (Entry<String, List<Rule>> entry : inputTestCaseMap.entrySet()) {

					if (tagTestCase(entry.getValue(), body)) {

						Response response = httpClient.send(schema.getSource(), schema.getRequestMethod(),
								schema.getContentType(), schema.getReturnType(), ServicesUtility.ObjectToString(body),
								schema.getAuthorizationKey(), schema.isSecure());

						testsToValidate.add(Test.builder().testId(entry.getKey()).response(response)
								.vadilationRules(outputTestCaseMap.get(entry.getKey())).build());

					}

				}

			}

			responseValidator.service(testsToValidate, reportId);

			LOGGER.info("Request for " + reportId + " is now complete!!!");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param rules
	 * @param object
	 * @return
	 */
	private boolean tagTestCase(List<Rule> rules, Object object) {

		for (Rule rule : rules) {

			if (!ruleEngine.process(rule, object).equals(ServicesConstants.RULE_PASS))
				return false;

		}

		return true;

	}

}