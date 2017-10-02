package uk.co.olimor.BMBTApi_Common.security;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;


/**
 * Test class for the JWT Token.
 * 
 * @author leonmorley
 *
 */
public class JWTUtilTest {

	@Test
	public void testGetJWT() {
		final String deviceId = "device-id";
		
		final JWTUtil util = new JWTUtil();
		
		Whitebox.setInternalState(util, "secret", "jwtsecret");
		
		final String token = util.generateJWTToken(deviceId);
		
		Assert.assertNotNull(token);
		
		Assert.assertEquals(deviceId, util.getSubjectFromToken(token));
	}
	
}
