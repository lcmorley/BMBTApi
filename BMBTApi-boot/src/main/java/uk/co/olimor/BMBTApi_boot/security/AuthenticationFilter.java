package uk.co.olimor.BMBTApi_boot.security;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.common.Constants;

/**
 * Filter class to process authentication requests.
 * 
 * @author leonmorley
 */
@Log4j2
public class AuthenticationFilter extends GenericFilterBean {

	/**
	 * {@link SecurityUtil} instance.
	 */
	private SecurityUtil securityUtil;
	
	/**
	 * JWT_TOKEN constant.
	 */
	private static final String JWT_TOKEN = "JWT_TOKEN";
	
	/**
	 * Header constant to state this is a register.
	 */
	private static final String NEW_DEVICE = "NEW_DEVICE";

	/**
	 * Constructor.
	 * 
	 * @param securityUtil - the {@link SecurityUtil} instance.
	 */
	public AuthenticationFilter(final SecurityUtil securityUtil) {
		this.securityUtil = securityUtil;
	}
	
	/**
	 * Perform authentication on the incoming request.
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		log.traceEntry();
		
		Authentication authenticated;
		
		Enumeration<String> headers = ((HttpServletRequest) request).getHeaderNames();
		
		while (headers.hasMoreElements()) {
			System.out.println(headers.nextElement());
		}
		
		final String newDevice = ((HttpServletRequest) request).getHeader(NEW_DEVICE);	
		
		if (newDevice != null) {
			log.debug("Device id found: " + newDevice);
			processNewDevice(newDevice, request, response, chain);
			log.traceExit();
			return;
		} 
		
		log.debug("No device id found.");
		
		final String token = ((HttpServletRequest) request).getHeader(JWT_TOKEN);
		
		log.debug("Token is: " + token);
		
		if (token != null && !token.isEmpty()) {
			try {
				authenticated = securityUtil.authenticate(token);
			} catch (final SecurityException e) {
				log.error("Authentication failed");
				authenticated = null;
			}
		} else {
			authenticated = null;
		}
			
		SecurityContextHolder.getContext().setAuthentication(authenticated);
		
		chain.doFilter(request, response);
		
		log.traceExit();
	}

	/**
	 * Process a new device request.
	 * 
	 * @param request - the request containing the device information.
	 * @param response - the response.
	 * @param chain - the filter chain.
	 * 
	 * @throws IOException - the exception raised. 
	 * @throws ServletException - the exception raised.
	 */
	private void processNewDevice(final String deviceEncoded, final ServletRequest request, 
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		log.entry(deviceEncoded);
		
		final String deviceDecoded = new String(Base64.getDecoder().decode(deviceEncoded));		
		final String[] deviceParts = deviceDecoded.split("\\.");
		boolean deviceDecodedValid = false;
		
		if (deviceParts.length == 2) {
			if (deviceParts[0].equals(Constants.NEW_DEVICE)) {
				final String deviceId = deviceParts[1];				
				request.setAttribute(Constants.DEVICE, deviceId);				
				SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
						deviceId , null, Collections.emptyList()));	
				deviceDecodedValid = true;
			}		
		} 
		
		if (!deviceDecodedValid)
			log.error("Invalid device information found on request.");
		
		chain.doFilter(request, response);
		
		log.traceExit();
	}

}
