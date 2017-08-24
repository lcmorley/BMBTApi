package uk.co.olimor.BMBTApi_boot.dao;

import uk.co.olimor.BMBTApi_boot.exception.ApiException;
import uk.co.olimor.BMBTApi_boot.model.TestResult;

/**
 * Interface to allow a {@link TestResult} to be persisted.
 * 
 * @author leonmorley
 *
 */
public interface TestResultInsert {

	/**
	 * Save the {@link TestResult} object to the database.
	 * 
	 * @param result - the result to save.
	 * 
	 * @return - the number of rows inserted.
	 */
	void saveTestResult(final TestResult result) throws ApiException;
	
}
