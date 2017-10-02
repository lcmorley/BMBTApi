package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.common.Constants;
import uk.co.olimor.BMBTApi_boot.dao.AbstractDAO;
import uk.co.olimor.BMBTApi_Common.exception.ApiException;

/**
 * Abstract class containing common functionality.
 * 
 * @author leonmorley
 *
 */
@Log4j2
public abstract class AbstractUpdate<T> extends AbstractDAO {

	/**
	 * Datasource object.
	 */
	@Autowired
	protected DataSource datasource;
		
	/**
	 * 
	 * @param objectToUpdate
	 * @throws ApiException 
	 */
	public int update(final T objectToUpdate) throws ApiException {
		log.entry(objectToUpdate);
		
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = datasource.getConnection();
			stmt = conn.createStatement();
			
			final int result = stmt.executeUpdate(buildUpdate(objectToUpdate));
			
			if (result == 0)
				logError(log, "The object was not updated on the db. Value: " + objectToUpdate, null, 
						HttpStatus.INTERNAL_SERVER_ERROR);
			
			return log.traceExit(result);	
		} catch (final SQLException e) {
			logError(log, "An error occurred whilst attempting to update on the database with object: " 
				+ objectToUpdate, e, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (stmt != null)
					stmt.close();
			} catch (final SQLException e) {
				log.error("An error occurred whilst attempting to update on the database with object: " 
					+ objectToUpdate, e);
			}
		}
		
		return log.traceExit(Constants.INT_ZERO);
	}

	/**
	 * Given an object of T, build and return the update string.
	 * 
	 * @param objectToUpdate - the object to update into the db.
	 * 
	 * @return - the number of results inserted.
	 */
	protected abstract String buildUpdate(T objectToUpdate);

}
