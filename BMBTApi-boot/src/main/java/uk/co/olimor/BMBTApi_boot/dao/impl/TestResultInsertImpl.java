package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.TestResultInsert;
import uk.co.olimor.BMBTApi_boot.exception.ApiException;
import uk.co.olimor.BMBTApi_boot.model.TestResult;

/**
 * Persist the test result to the database.
 * 
 * @author leonmorley
 */
@Service
@Log4j2
public class TestResultInsertImpl extends AbstractInsert<TestResult>implements TestResultInsert {

	/**
	 * Insert statement with placeholders.
	 */
	private static final String INSERT_STATEMENT = "INSERT INTO testResult VALUES (%s, %d, %d, %d, %f, '%s')";	
	
	/**
	 * Save the {@link TestResult} object to the database.
	 * 
	 * @param result - the {@link TestResult} to save.
	 * @throws ApiException 
	 */
	@Override
	public void saveTestResult(final TestResult result) throws ApiException {
		log.entry(result);
		insert(result);
		log.traceExit();
	}

	/**
	 * Method to build the insert method given a {@link TestResult}.
	 */
	protected String buildInsert(final TestResult result) {
		log.entry(result);
		
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		final String currentTime = format.format(new Date());
		
		return log.traceExit(String.format(INSERT_STATEMENT, result.getUserId(), result.getTestId(), 
				result.getCorrectAnswers(), result.getIncorrectAnswers(), result.getElapsedTime(), currentTime));
	}

}
