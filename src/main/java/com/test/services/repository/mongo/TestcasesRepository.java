package com.test.services.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.test.services.model.mongo.Testcase;

public interface TestcasesRepository extends MongoRepository<Testcase, String> {

	List<Testcase> findByTestIdLike(String testId);

	Testcase findOneByTestId(String testId);

}