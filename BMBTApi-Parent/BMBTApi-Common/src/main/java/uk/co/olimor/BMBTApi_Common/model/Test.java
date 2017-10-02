package uk.co.olimor.BMBTApi_Common.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class which represents a Test.
 * 
 * @author leonmorley
 *
 */
@Data
@AllArgsConstructor
public class Test {

	/**
	 * Id.
	 */
	private int id;
	
	/**
	 * Name of test.
	 */
	private String name;
	
	/**
	 * Number of seconds.
	 */
	private int numberOfSeconds;
	
	/**
	 * List of questions. 
	 */
	private List<Question> questions;
	
}
