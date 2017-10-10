package uk.co.olimor.BMBTApi_Common.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.common.Constants;
import uk.co.olimor.BMBTApi_Common.dao.AbstractDAO;
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
	public void update(final T objectToUpdate) throws ApiException {
		log.entry(objectToUpdate);

		try (final Connection conn = datasource.getConnection(); final Statement stmt = conn.createStatement();) {
			if (stmt.executeUpdate(buildUpdate(objectToUpdate)) == Constants.INT_ZERO)
				logError(log, "The object was not updated on the db. Value: " + objectToUpdate, null,
						HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (final SQLException e) {
			logError(log,
					"An error occurred whilst attempting to update on the database with object: " + objectToUpdate, e,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.traceExit();
	}

	/**
	 * Given an object of T, build and return the update string.
	 * 
	 * @param objectToUpdate
	 *            - the object to update into the db.
	 * 
	 * @return - the number of results inserted.
	 */
	protected abstract String buildUpdate(T objectToUpdate);

}
