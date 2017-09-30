package uk.co.olimor.BMBTApi_boot.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.common.Constants;
import uk.co.olimor.BMBTApi_boot.model.LoginCredentials;

/**
 * Filter for login request to check that the input contains the correct user and password.
 * 
 * @author leonmorley
 *
 */
@Log4j2
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	/**
	 * Constructor for Filter.
	 * 
	 * @param url - the url which is invoking this filter.
	 * 
	 * @param authManager
	 */
	public LoginFilter(final String url, final AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		log.traceEntry();
		
		setAuthenticationManager(authManager);

		log.traceExit();
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest req, final HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {
		log.traceEntry();
		
		final LoginCredentials credentials = new ObjectMapper().readValue(req.getInputStream(), LoginCredentials.class);
		req.setAttribute(Constants.DEVICE, credentials.getDeviceId());
		
		log.debug("Login attempt for device: " + credentials.getDeviceId());
		
		log.traceExit();
		
		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(credentials.getUserId(),
			credentials.getPassword(), Collections.emptyList()));							
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		log.traceEntry();		
		chain.doFilter(req, res);
		log.traceExit();
	}
	
}