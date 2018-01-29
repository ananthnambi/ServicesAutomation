package com.test.services.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.test.services.model.mongo.Report;

public interface ReportRepository extends MongoRepository<Report, Long> {

	List<Report> findByReportId(String reportId);

	List<Report> findByReportIdAndTestIdAndStatus(String reportId, String testId, String status);

}