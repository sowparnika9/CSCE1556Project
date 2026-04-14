package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Carlos Bueno and Sowparnika Sandhya
 * Date:2023-04-14
 * 
 * A collection of helper methods to open and close a database connection.
 *
 */
public class DatabaseInfo {

	public static final String PARAMETERS = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	public static final String USERNAME = "cbueno";
	public static final String PASSWORD = "gNBKfLb3";
	public static final String URL = "jdbc:mysql://cse.unl.edu/" + USERNAME + PARAMETERS;
	private static Logger LOGGER = LogManager.getLogger();

	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			LOGGER.error("SQLException : Couldn't create connection.");
			throw new RuntimeException(e);
		}
		return conn;
	}

	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}