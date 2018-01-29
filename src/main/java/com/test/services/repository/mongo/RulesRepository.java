package com.test.services.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.test.services.model.mongo.Rule;

public interface RulesRepository extends MongoRepository<Rule, String> {

	List<Rule> findByRuleIdIn(List<String> ruleId);

	List<Rule> findByTypeAndRuleId(String type, String ruleId);

}