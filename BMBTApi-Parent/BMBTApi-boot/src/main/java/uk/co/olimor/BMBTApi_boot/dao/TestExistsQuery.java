package uk.co.olimor.BMBTApi_boot.dao;

import uk.co.olimor.BMBTApi_Common.exception.ApiException;

/**
 * Contract for the TestExistsQueryImpl class.
 * 
 * @author leonmorley
 *
 */
public interface TestExistsQuery {

	/**
	 * Check to see if the text exists.
	 * 
	 * @param testId - the test id.
	 * 
	 * @return true if the test is found, otherwise false.
	 */
	boolean testExists(int testId) throws ApiException;
	
}
