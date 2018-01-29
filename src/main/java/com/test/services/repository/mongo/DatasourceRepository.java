package com.test.services.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.test.services.model.mongo.Datasource;

public interface DatasourceRepository extends MongoRepository<Datasource, Long> {

	Datasource findOneBySourceName(String sourceName);

}