package com.test.services.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.test.services.model.mongo.Schema;

public interface SchemaRepository extends MongoRepository<Schema, Long> {

	Schema findOneByTestSuite(String testSuite);

}