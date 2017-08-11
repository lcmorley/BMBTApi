package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.TestResultInsert;
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
	private static final String INSERT_STATEMENT = "INSERT INTO testResult VALUES (%1, %2, %3, %4, %5, %6, %7)";	
	
	/**
	 * Save the {@link TestResult} object to the database.
	 * 
	 * @param result - the {@link TestResult} to save.
	 */
	@Override
	public int saveTestResult(final TestResult result) {
		log.entry(result);
		return log.traceExit(insert(result));
	}

	/**
	 * Method to build the insert method given a {@link TestResult}.
	 */
	protected String buildInsert(final TestResult result) {
		log.entry(result);
		
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		final String currentTime = format.format(new Date());
		
		return log.traceExit(String.format(INSERT_STATEMENT, result.getUserId(), result.getTestId(), 
				result.getCorrectAnswers(), result.getIncorrectAnswers(), result.getAttemptedAnswers(), 
				result.getElapsedTime(), currentTime));
	}

}