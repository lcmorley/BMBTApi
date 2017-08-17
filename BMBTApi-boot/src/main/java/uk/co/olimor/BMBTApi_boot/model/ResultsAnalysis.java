package uk.co.olimor.BMBTApi_boot.model;

import lombok.Data;

/**
 * Object which contains the results analysis.
 * 
 * @author leonmorley
 *
 */
@Data
public class ResultsAnalysis {

	/**
	 * Total Tests carried out.
	 */
	private int totalTests;
	
	/**
	 * Average number of attempted questions.
	 */
	private int averageAttemptedQuestions;

	/**
	 * Average number of correct answers.
	 */
	private int averageCorrectAnswers;
	
	/**
	 * The highest number of correct answers.
	 */
	private int topCorrectAnswers;
	
	/**
	 * Average time taken to perform the test.
	 */
	private float averageTime;
	
	/**
	 * The best time.
	 */
	private float bestTime;
	
}
