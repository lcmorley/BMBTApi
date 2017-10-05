package uk.co.olimor.BMBTApi_Common.exception;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;


/**
 * Test class for the {@link ApiException} class.
 * 
 * @author leonmorley
 *
 */
public class ApiExceptionTest {

	@Test
	public void testConstructor() {
		
		final Throwable exception = new NullPointerException();
		final ApiException ex = new ApiException("A null pointer occurred", exception, 
				HttpStatus.ACCEPTED);
		
		Assert.assertEquals("A null pointer occurred", ex.getMessage());
		Assert.assertEquals(exception, ex.getCause());
		Assert.assertEquals(HttpStatus.ACCEPTED, ex.getStatus());
		
	}
	
}
