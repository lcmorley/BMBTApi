package uk.co.olimor.lambda.user_service.utility;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for the {@link PropertyUtil} class.
 * 
 * @author leonmorley
 *
 */
public class PropertyUtilTest {

	@Test
	public void testGetProperty() {
		Assert.assertEquals("property", PropertyUtil.getProperty("test"));
	}
	
}
