package com.test.services.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.services.model.json.DetailedReport;
import com.test.services.model.json.TestReport;
import com.test.services.model.mongo.Report;
import com.test.services.model.mongo.Schema;
import com.test.services.model.mongo.Testcase;
import com.test.services.repository.mongo.ReportRepository;
import com.test.services.repository.mongo.SchemaRepository;
import com.test.services.repository.mongo.TestcasesRepository;
import com.test.services.util.ServicesConstants;
import com.test.services.util.ServicesUtility;

/**
 * @author ananthnambi@gmail.com
 *
 */
@Service
public class WebClientService {

	/**
	 * LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebClientService.class);

	/**
	 * 
	 */
	@Autowired
	private TestcasesRepository testcasesRepository;

	/**
	 * 
	 */
	@Autowired
	private ReportRepository reportRepository;

	/**
	 * 
	 */
	@Autowired
	private SchemaRepository schemaRepository;

	/**
	 * @return
	 */
	public List<String> fetchConfiguredTestSuites() {

		List<String> tests = new ArrayList<String>();

		try {

			for (Schema schema : schemaRepository.findAll()) {

				tests.add(schema.getTestSuite());

			}

		} catch (Exception ex) {

			LOGGER.error("Exception in fetchConfiguredTestSuites", ex);

		}

		return tests;
	}

	/**
	 * @param reportId
	 * @return
	 */
	public List<TestReport> getReport(String reportId) {

		List<TestReport> result = null;

		try {

			Map<String, TestReport> map = new HashMap<String, TestReport>();

			List<Report> reports = reportRepository.findByReportId(reportId);

			for (Report report : reports) {

				TestReport testReport = map.get(report.getTestId());

				boolean pass = report.getStatus().equals(ServicesConstants.TEST_CASE_PASS);

				if (testReport == null) {

					Testcase testcase = testcasesRepository.findOneByTestId(report.getTestId());

					testReport = TestReport.builder().testId(testcase.getTestId()).description(testcase.getTestDesc())
							.noOfValidations(1).passCount(pass ? 1 : 0).failCount(!pass ? 1 : 0).build();

					map.put(testcase.getTestId(), testReport);

				} else {

					testReport.setNoOfValidations(testReport.getNoOfValidations() + 1);

					if (pass)
						testReport.setPassCount(testReport.getPassCount() + 1);
					else
						testReport.setFailCount(testReport.getFailCount() + 1);

				}

			}

			result = new ArrayList<TestReport>(map.values());

			calculatePercentage(result);

		} catch (Exception ex) {

			LOGGER.error("Exception in getReport", ex);

		}

		return result;

	}

	/**
	 * @param values
	 */
	private void calculatePercentage(List<TestReport> values) {

		for (TestReport report : values) {

			double passPerc = ((double) report.getPassCount() / (double) report.getNoOfValidations()) * 100;

			report.setPassPercentage(passPerc);
			report.setFailPercentage(100 - passPerc);

		}

	}

	/**
	 * @param id
	 * @param test
	 * @param status
	 * @return
	 */
	public List<DetailedReport> getDetailedReport(String id, String test, String status) {

		List<DetailedReport> result = new ArrayList<DetailedReport>();

		try {

			List<Report> reports = reportRepository.findByReportIdAndTestIdAndStatus(id, test, status);

			for (Report report : reports) {

				result.add(DetailedReport.builder().request(report.getRequest()).response(report.getResponse())
						.responseCode(Integer.toString(report.getResponseCode())).failedRules(report.getFailedRules())
						.dateTime(ServicesUtility.getStringDateTime(report.getTimestamp())).build());

			}

		} catch (Exception ex) {

			LOGGER.error("Exception in getDetailedReport", ex);

		}

		return result;

	}

}
