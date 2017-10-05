package uk.co.olimor.BMBTApi_Common.response;

import org.junit.Assert;
import org.junit.Test;

import uk.co.olimor.BMBTApi_Common.model.DatabaseToken;
import uk.co.olimor.BMBTApi_Common.response.ApiResponse;
import uk.co.olimor.BMBTApi_Common.response.ApiResponseError;

/**
 * Test class for the {@link ApiResponse} object.
 * 
 * @author leonmorley
 *
 */
public class ApiResponseTest {

	@Test
	public void testConstructor() {
		final Object responseObject = new DatabaseToken();
		final ApiResponseError error = new ApiResponseError();
		
		ApiResponse response = new ApiResponse(responseObject);		
		Assert.assertEquals(responseObject, response.getObject());
		
		response = new ApiResponse("Confirmation");
		Assert.assertEquals("Confirmation", response.getConfirmationMessage());
		
		response = new ApiResponse(responseObject, error);
		Assert.assertEquals(responseObject, response.getObject());
		Assert.assertEquals(error, response.getError());
	}
	
}
