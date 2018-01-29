package com.test.services.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.test.services.model.mongo.Config;

public interface ConfigRepository extends MongoRepository<Config, Long> {

	Config findOneByServiceName(String serviceName);

	Config findOneByName(String name);

}