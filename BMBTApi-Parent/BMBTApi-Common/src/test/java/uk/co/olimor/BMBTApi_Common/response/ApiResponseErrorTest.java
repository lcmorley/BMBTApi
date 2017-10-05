package uk.co.olimor.BMBTApi_Common.response;

import org.junit.Assert;
import org.junit.Test;

import uk.co.olimor.BMBTApi_Common.exception.ApiException;
import uk.co.olimor.BMBTApi_Common.response.ApiResponseError;

/**
 * Test class for the {@link ApiResponseError} class.
 * 
 * @author leonmorley
 *
 */
public class ApiResponseErrorTest {

	@Test
	public void testConstructor() {
		final ApiException ex = new ApiException();
		
		final ApiResponseError error = new ApiResponseError(ex);
		
		Assert.assertEquals(ex.getMessage(), error.getErrorMessage());
		Assert.assertEquals(ex.getUUID(), error.getUUID());
	}
	
}
