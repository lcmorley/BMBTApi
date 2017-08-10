package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * Abstract class containing common functionality.
 * 
 * @author leonmorley
 *
 */
@Data
@Log4j2
public abstract class AbstractInsert<T> {

	/**
	 * Datasource object.
	 */
	@Autowired
	protected MysqlDataSource datasource;
		
	/**
	 * 
	 * @param objectToInsert
	 */
	public int insert(final T objectToInsert) {
		log.entry(objectToInsert);
		
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = datasource.getConnection();
			stmt = conn.createStatement();

			return log.traceExit(stmt.executeUpdate(buildInsert(objectToInsert)));	
		} catch (final SQLException e) {
			log.error("An error occurred whilst attempting to insert into the database with object: " 
				+ objectToInsert, e);
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (stmt != null)
					stmt.close();
			} catch (final SQLException e) {
				log.error("An error occurred whilst attempting to insert into the database with object: " 
					+ objectToInsert, e);
			}
		}
		
		return log.traceExit(0);
	}

	/**
	 * Given an object of T, build and return the insert string.
	 * 
	 * @param objectToInsert - the object to insert into the db.
	 * 
	 * @return - the number of results inserted.
	 */
	protected abstract String buildInsert(T objectToInsert);

}
