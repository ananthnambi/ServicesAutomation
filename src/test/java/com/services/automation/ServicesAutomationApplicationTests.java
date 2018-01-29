package com.services.automation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.test.services.ServicesAutomation;
import com.test.services.controller.TestController;
import com.test.services.model.mongo.Report;
import com.test.services.repository.mongo.ReportRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServicesAutomation.class)
public class ServicesAutomationApplicationTests {

	@Autowired
	private TestController testController;

	@Autowired
	private ReportRepository reportRepository;

	@Test
	public void testDPS() throws Exception {

		String reportId = testController.trigger("Sample-1");

		List<Report> reports = null;

		for (int i = 0; i < 10; i++) {

			reports = reportRepository.findByReportId(reportId);

			if (reports != null && !reports.isEmpty())
				break;

			Thread.sleep(30000);

		}

		assertThat(reports).isNotNull().isNotEmpty();

	}

}
