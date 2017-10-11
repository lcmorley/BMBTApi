package uk.co.olimor.BMBTApi_Common.dao;

import javax.sql.DataSource;

import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import uk.co.olimor.BMBTApi_Common.exception.ApiException;

/**
 * Abstract class which contains common functionality for all DAO objects.
 * 
 * @author leonmorley
 *
 */
public abstract class AbstractDAO {

	/**
	 * Datasource object.
	 */
	protected DataSource datasource;

	/**
	 * Log and throw error.
	 * 
	 * @param log - the log to log to.
	 * @param message - the message to log and throw.
	 * @param status - the HttpStatus to return.
	 * 
	 * @throws ApiException - the exception thrown.
	 */
	protected void logError(final Logger log, final String message, final HttpStatus status) throws ApiException {
		log.error(message);
		throw new ApiException(message, status);
	}
	
	/**
	 * Log and throw error.
	 * 
	 * @param log - the log to log to.
	 * @param message - the message to log and throw.
	 * @param cause - the exception that caused this.
	 * 
	 * @throws ApiException - the exception thrown.
	 */
	protected void logError(final Logger log, final String message, final Throwable cause, final HttpStatus status) 
			throws ApiException {
		log.error(message, cause);
		throw new ApiException(message, cause, status);
	}
	
}
