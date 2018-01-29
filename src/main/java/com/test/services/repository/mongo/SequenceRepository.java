package com.test.services.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.test.services.model.mongo.Sequence;

public interface SequenceRepository extends MongoRepository<Sequence, String> {

}