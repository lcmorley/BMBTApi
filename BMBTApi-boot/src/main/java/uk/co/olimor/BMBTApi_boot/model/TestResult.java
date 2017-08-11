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
	private int userId;
	
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
	 * The number of attempted answers.
	 */
	private int attemptedAnswers;
	
	/**
	 * Elapsed time for the completion of the test.
	 */
	private float elapsedTime;
	
}