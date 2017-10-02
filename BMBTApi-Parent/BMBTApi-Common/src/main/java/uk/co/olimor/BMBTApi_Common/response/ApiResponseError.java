package uk.co.olimor.BMBTApi_Common.response;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.exception.ApiException;

/**
 * 
 * @author leonmorley
 *
 */
@Data
@Log4j2
public class ApiResponseError {

	/**
	 * Unique ID.
	 */
	private String UUID;
	
	/**
	 * Error message.
	 */
	private String errorMessage;
	
	/**
	 * Constructor.
	 * 
	 * @param message
	 */
	public ApiResponseError(final ApiException e) {
		log.entry(e);
		
		this.errorMessage = e.getMessage();
		this.UUID = e.getUUID();
		
		log.traceExit();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param errorMessage - the error message.
	 */
	public ApiResponseError(final String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	/**
	 * Default Constructor.
	 */
	public ApiResponseError() {}
	
}
