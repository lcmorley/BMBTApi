package uk.co.olimor.BMBTApi_Common.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Exception class for API exceptions.
 * 
 * @author leonmorley
 *
 */
@Log4j2
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class ApiException extends Exception {

	/**
	 * Default Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * UUID value.
	 */
	private String UUID;
	
	/**
	 * Throwable object.
	 */
	private Throwable throwable;
	
	/**
	 * Status.
	 */
	private HttpStatus status;
	
	/**
	 * Constructor.
	 * 
	 * @param message - exception message.
	 * @param cause - the cause of the exception.
	 * @param status - the HttpStatus to return.
	 * 
	 */
	public ApiException(final String message, final Throwable cause, final HttpStatus status) {
		super(message, cause);
		
		this.UUID = java.util.UUID.randomUUID().toString();
		this.status = status;
		
		log.debug(message, cause, UUID, status);
	}
	
	/**
	 * Constructor for message and status.
	 * 
	 * @param message - the exception message.
	 * @param status - the status to return.
	 */
	public ApiException(final String message, final HttpStatus status) {
		super(message);
		
		this.UUID = java.util.UUID.randomUUID().toString();
		this.status = status;
		
		log.debug(message, status);
	}
	
}
