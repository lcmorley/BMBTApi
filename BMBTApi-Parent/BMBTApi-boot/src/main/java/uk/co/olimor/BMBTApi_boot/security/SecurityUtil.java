package uk.co.olimor.BMBTApi_boot.security;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.exception.ApiException;
import uk.co.olimor.BMBTApi_Common.model.DatabaseToken;
import uk.co.olimor.BMBTApi_Common.security.JWTUtil;
import uk.co.olimor.BMBTApi_boot.dao.TokenCheckQuery;

/**
 * Class used to provide the security implementation for the API.
 * 
 * @author leonmorley
 *
 */
@Log4j2
@Service
public class SecurityUtil {
	
	/**
	 * JWT Utility.
	 */
	@Autowired
	private JWTUtil jwtUtil;
	
	/**
	 * Query to retrieve the token from the DB.
	 */
	@Autowired
	private TokenCheckQuery tokenCheck;
	
	/**
	 * Given the token, check that the caller is genuine.
	 * 
	 * @param token - the JWT token to check.
	 */
	public Authentication authenticate(final String token) throws SecurityException {
		log.entry(token);
		
		final String deviceId = jwtUtil.getSubjectFromToken(token);
		
		DatabaseToken dbToken = null;		
		
		try {
			dbToken = tokenCheck.getTokenFromDeviceId(deviceId);
		} catch (final ApiException e) {
			log.error("Problem occurred whilst attempting to retrieve the token from the db.", e);
			throw new SecurityException();
		}
		
		log.debug("Token returned: " + dbToken);
		
		if (!dbToken.getToken().equals(token)) {
			log.error("Token does not match that found on the DB.");
			throw new SecurityException();
		}
 		
		final Calendar tokenExpiry = Calendar.getInstance();
		tokenExpiry.setTime(dbToken.getExpiryDate());
		
		final Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		
		if (now.after(tokenExpiry)) {
			log.error("Token expired");
			throw new SecurityException();
		}
		
		log.info("Device: " + deviceId + " successfully authenticated.");
		
		return new UsernamePasswordAuthenticationToken(deviceId, null, Collections.emptyList());
	}
	
}
