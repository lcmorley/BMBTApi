package uk.co.olimor.BMBTApi_boot.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

/**
 * Utility class to generate and validate JWT tokens.
 * 
 * @author leonmorley
 */
@Log4j2
@Service
public final class JWTUtil {

	/**
	 * JWT Secret.
	 */
	@Value("${jwt.secret}")
	private String secret;
	
	/**
	 * Generate the JWT to return.
	 * 
	 * @param deviceId - the deviceId
	 * 
	 * @return - the JWT.
	 */
	public String generateJWTToken(final String deviceId) {		
		log.traceEntry();
		return Jwts.builder()
			.setSubject(deviceId)
			.signWith(SignatureAlgorithm.HS512, secret.getBytes())
			.compact();		
	}
	
	/**
	 * Validate the JWT Token.
	 * 
	 * @param token - the token to validate.
	 * 
	 * @return - the token.
	 */
	public String getSubjectFromToken(final String token) {
		log.entry(token);
		return log.traceExit(Jwts.parser().setSigningKey(secret.getBytes())
				.parseClaimsJws(token)
				.getBody()
				.getSubject());				
	}
	
}
