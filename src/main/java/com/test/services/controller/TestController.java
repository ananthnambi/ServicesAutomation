package com.test.services.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.services.processor.custom.RequestProcessor;
import com.test.services.util.ConfigService;
import com.test.services.util.ServicesConstants;

@RestController
@RequestMapping(value = "/trigger")
public class TestController {

	/**
	 * LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

	/**
	 * ApplicationContext reference to access any spring managed bean.
	 */
	@Autowired
	private ApplicationContext appContext;

	/**
	 * This attribute is used to manage the thread pool and to execute tasks
	 * concurrently
	 */
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	/**
	 * 
	 */
	@Autowired
	private ConfigService configService;

	/**
	 * @param test
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public String trigger(@RequestParam(required = true) String test) {

		String reportId = configService.getId(ServicesConstants.REPORT_ID_USE);

		LOGGER.info("Processing " + test + " for id " + reportId);

		RequestProcessor requestProcessor = (RequestProcessor) appContext
				.getBean(ServicesConstants.BEAN_REQUEST_PROCESSOR, test, reportId);

		taskExecutor.execute(requestProcessor);

		return reportId;

	}

}