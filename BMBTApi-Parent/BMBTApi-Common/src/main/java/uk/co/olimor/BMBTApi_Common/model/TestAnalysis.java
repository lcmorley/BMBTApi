package uk.co.olimor.BMBTApi_Common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Analysis of results for a given test.
 * 
 * @author leonmorley
 *
 */
@Data
@NoArgsConstructor
public class TestAnalysis {

	/**
	 * Id of the test which this analysis applies to.
	 */
	private int testId;
	
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
	
	/**
	 * Constructor.
	 * 
	 * @param testId
	 */
	public TestAnalysis(final int testId) {
		this.testId = testId;
	}
	
}
