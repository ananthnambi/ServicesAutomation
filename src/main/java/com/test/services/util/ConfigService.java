package com.test.services.util;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.test.services.model.mongo.Config;
import com.test.services.model.mongo.Sequence;
import com.test.services.repository.mongo.ConfigRepository;
import com.test.services.repository.mongo.SequenceRepository;

@Configuration
public class ConfigService extends WebMvcConfigurerAdapter {

	/**
	 * LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigService.class);

	/**
	 * 
	 */
	@Autowired
	private SequenceRepository sequenceRepository;

	/**
	 * 
	 */
	@Autowired
	private ConfigRepository configRepository;

	/**
	 * @param use
	 * @return
	 */
	public String getId(String use) {

		LOGGER.info("Get id for use : " + use);

		Sequence sequence = sequenceRepository.findOne(use);

		int id = sequence.getCurrentGenId();

		sequence.setCurrentGenId(sequence.getCurrentGenId() + 1);

		sequenceRepository.save(sequence);

		return UUID.randomUUID().toString() + "-" + id;

	}

	/**
	 * @param serviceName
	 * @return
	 */
	public String getDefaultDatasource(String serviceName) {

		LOGGER.info("Fetch default datasource for service : " + serviceName);

		Config config = configRepository.findOneByServiceName(serviceName);

		if (config != null)

			return config.getDatasource();

		else

			return null;

	}

	/**
	 * To get external properties required by the services.
	 * 
	 * @param propertyName
	 * @return propertyValue
	 */
	public String getProperty(String propertyName) {

		LOGGER.info("Fetching property : " + propertyName);

		Config config = configRepository.findOneByName(propertyName);

		if (config != null)

			return config.getValue();

		else

			return null;

	}

	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {

		LOGGER.info("Initializing thread pool...");

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(100);
		executor.setWaitForTasksToCompleteOnShutdown(true);

		return executor;

	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/").setViewName("forward:/index.html");

	}

}