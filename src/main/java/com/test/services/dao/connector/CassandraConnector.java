package com.test.services.dao.connector;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Class used for connecting to Cassandra database.
 */
public class CassandraConnector {

	/** Cassandra Cluster. */
	private Cluster cluster;

	/** Cassandra Session. */
	private Session session;

	/** keyspace to query. */
	private String keyspace;

	/**
	 * Getter for keyspace
	 * 
	 * @return keyspace
	 */
	public String getKeyspace() {

		return keyspace;

	}

	/**
	 * Constr - Connect to Cassandra Cluster specified by provided node IP
	 * address and port number.
	 *
	 * @param node
	 *            Cluster node IP address.
	 * @param port
	 *            Port of cluster host.
	 */
	public CassandraConnector(final String node, final int port, final String username, final String password,
			String keyspace) {

		this.cluster = Cluster.builder().addContactPoint(node).withPort(port).withCredentials(username, password)
				.build();

		session = cluster.connect();

		this.keyspace = keyspace;

	}

	/**
	 * Provide my Session.
	 *
	 * @return My session.
	 */
	public Session getSession() {

		return this.session;

	}

	/** Close cluster. */
	public void close() {

		cluster.close();

	}
}