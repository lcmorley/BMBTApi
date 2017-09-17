package uk.co.olimor.BMBTApi_boot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class which holds the test result for a given test.
 * 
 * @author leonmorley
 *
 */
@Data
@AllArgsConstructor
public class TestResult {

	/**
	 * Id of the user who performed the test.
	 */
	private String userId;
	
	/**
	 * The id of the test which was performed.
	 */
	private int testId;
	
	/**
	 * The number of correct answers.
	 */
	private int correctAnswers;
	
	/**
	 * The number of incorrect answers.
	 */
	private int incorrectAnswers;
	
	/**
	 * Elapsed time for the completion of the test.
	 */
	private float elapsedTime;
	
	/**
	 * Type of test (Training or Full).
	 */
	private String testType;
	
}
