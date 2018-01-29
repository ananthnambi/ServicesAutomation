package com.test.services.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.services.model.mongo.Report;
import com.test.services.model.mongo.Rule;
import com.test.services.model.service.Response;
import com.test.services.model.service.Test;
import com.test.services.repository.mongo.ReportRepository;
import com.test.services.util.ServicesConstants;

@Service
public class ResponseValidator {

	/**
	 * LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseValidator.class);

	@Autowired
	private RuleEngine ruleEngine;

	@Autowired
	private ReportRepository reportRepository;

	public void service(List<Test> tests, String reportId) {

		LOGGER.info("Validating responses for " + reportId);

		for (Test test : tests) {

			List<String> failedRules = validateRules(test.getVadilationRules(), test.getResponse());

			Report report = new Report();

			report.setReportId(reportId);
			report.setTestId(test.getTestId());
			report.setRequest(test.getResponse().getRequest());
			report.setResponseCode(test.getResponse().getResponseCode());
			report.setResponse(test.getResponse().getResponse());
			report.setTimestamp(System.currentTimeMillis());

			if (failedRules.isEmpty()) {

				report.setStatus("PASS");

			} else {

				report.setStatus("FAIL");
				report.setFailedRules(failedRules);

			}

			reportRepository.save(report);

		}

	}

	/**
	 * @param rules
	 * @param response
	 * @return
	 */
	private List<String> validateRules(List<Rule> rules, Response response) {

		List<String> failedRules = new ArrayList<String>();

		for (Rule rule : rules) {

			String ruleEngineResponse = ruleEngine.process(rule, response);

			// failure returns failed rule
			if (!ruleEngineResponse.equals(ServicesConstants.RULE_PASS)) {

				if (ruleEngineResponse.equals(ServicesConstants.RULE_FAIL))
					failedRules.add("FAILED : " + rule.getRuleDesc());
				else
					failedRules.add(ruleEngineResponse);

			}

		}

		return failedRules;

	}

}