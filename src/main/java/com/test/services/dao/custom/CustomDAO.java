package com.test.services.dao.custom;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.services.dao.connector.CassandraConnector;
import com.test.services.model.mongo.Datasource;
import com.test.services.repository.mongo.DatasourceRepository;

@Component("dataAccessObject")
public class CustomDAO {

	private Map<String, Object> connectionMap = new ConcurrentHashMap<String, Object>();

	@Autowired
	private DatasourceRepository datasourceRepository;

	@PostConstruct
	private void init() {

		List<Datasource> sources = datasourceRepository.findAll();

		for (Datasource datasource : sources) {

			switch (datasource.getSourceType()) {

			case "cassandra":

				connectionMap.put(datasource.getSourceName(),
						new CassandraConnector(datasource.getHostIP(), datasource.getPort(), datasource.getUserName(),
								datasource.getPassword(), datasource.getKeyspace()));

				break;

			default:

				throw new RuntimeException("Invalid source type !!");

			}

		}

	}

	public Object execute(String query, String sourceName, Object... params) {

		Object resultSet = null;

		Object obj = connectionMap.get(sourceName);

		if (obj != null && obj instanceof CassandraConnector) {

			CassandraConnector connector = ((CassandraConnector) obj);

			// setting default keyspace
			connector.getSession().execute("use " + connector.getKeyspace());

			if (params != null) {

				// executing query
				resultSet = connector.getSession().execute(query, params);

			} else {

				// executing query
				resultSet = connector.getSession().execute(query);

			}

		}

		return resultSet;

	}

}