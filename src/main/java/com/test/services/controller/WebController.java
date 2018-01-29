package com.test.services.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.services.model.json.DetailedReport;
import com.test.services.model.json.TestReport;
import com.test.services.service.WebClientService;

@RestController
@RequestMapping(value = "/webclient")
public class WebController {

	/**
	 * LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);

	/**
	 * 
	 */
	@Autowired
	private WebClientService webClientService;

	/**
	 * @return
	 */
	@RequestMapping(value = "/fetch", method = RequestMethod.GET)
	public List<String> fetchTestSuites() {

		List<String> result = null;

		try {

			result = webClientService.fetchConfiguredTestSuites();

		} catch (Exception ex) {

			LOGGER.error("Exception in fetchTestSuites", ex);

		}

		return result;

	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/report", method = RequestMethod.GET)
	public List<TestReport> getReport(@RequestParam(required = true) String id) {

		List<TestReport> result = null;

		try {

			result = webClientService.getReport(id);

		} catch (Exception ex) {

			LOGGER.error("Exception in fetchTestSuites", ex);

		}

		return result;

	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/detailedreport", method = RequestMethod.GET)
	public List<DetailedReport> getDetailedReport(@RequestParam(required = true) String id,
			@RequestParam(required = true) String test, @RequestParam(required = true) String status) {

		List<DetailedReport> result = null;

		try {

			result = webClientService.getDetailedReport(id, test, status);

		} catch (Exception ex) {

			LOGGER.error("Exception in fetchTestSuites", ex);

		}

		return result;

	}

}