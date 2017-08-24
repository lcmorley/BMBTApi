package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.AbstractDAO;
import uk.co.olimor.BMBTApi_boot.exception.ApiException;

/**
 * Abstract class containing common functionality.
 * 
 * @author leonmorley
 *
 */
@Log4j2
public abstract class AbstractInsert<T> extends AbstractDAO {

	/**
	 * Datasource object.
	 */
	@Autowired
	protected MysqlDataSource datasource;
		
	/**
	 * 
	 * @param objectToInsert
	 * @throws ApiException 
	 */
	public void insert(final T objectToInsert) throws ApiException {
		log.entry(objectToInsert);
		
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = datasource.getConnection();
			stmt = conn.createStatement();
			
			final int result = stmt.executeUpdate(buildInsert(objectToInsert));
			
			if (result == 0)
				logError(log, "The object was not inserted into the db. Value: " + objectToInsert, null, 
						HttpStatus.INTERNAL_SERVER_ERROR);
			
			log.traceExit();	
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
		
		log.traceExit();
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
