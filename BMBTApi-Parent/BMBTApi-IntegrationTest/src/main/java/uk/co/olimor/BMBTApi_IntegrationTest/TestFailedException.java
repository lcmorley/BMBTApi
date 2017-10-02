package uk.co.olimor.BMBTApi_IntegrationTest;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Exception thrown when a test fails.
 * 
 * @author leonmorley
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class TestFailedException extends Exception {

	/**
	 * Default Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param api
	 */
	public TestFailedException(final String api) {
		super(api);
	}
	
}
