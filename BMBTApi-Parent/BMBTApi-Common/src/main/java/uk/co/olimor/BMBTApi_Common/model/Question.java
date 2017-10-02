package uk.co.olimor.BMBTApi_Common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Question class.
 * 
 * @author leonmorley
 *
 */
@Data
@AllArgsConstructor
public class Question {

	/**
	 * Question xValue
	 */
	private int xValue;
	
	/**
	 * Question yValue
	 */
	private int yValue;
	
	/**
	 * Question operator.
	 */
	private String operator;
	
}
