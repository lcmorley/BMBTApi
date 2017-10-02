package uk.co.olimor.BMBTApi_Common.response;

import lombok.Data;

/**
 * Api Response object.
 * 
 * @author leonmorley
 *
 */
@Data
public class ApiResponse {

	/**
	 * Response object.
	 */
	public Object object;
	
	/**
	 * Response error object.
	 */
	public ApiResponseError error;
	
	/**
	 * Confirmation message.
	 */
	public String confirmationMessage;
	
	/**
	 * Constructor for the ApiReponse.
	 * 
	 * @param object - the object.
	 * @param error - the error.
	 */
	public ApiResponse(final Object object, final ApiResponseError error) {
		this.object = object;
		this.error = error;
	}
	
	/**
	 * Constructor for the ApiReponse.
	 * 
	 * @param object - the object.
	 */
	public ApiResponse(final Object object) {
		this.object = object;
	}
	
	/**
	 * Constructor for the confirmation message.
	 * 
	 * @param confirmation - the message.
	 */
	public ApiResponse(final String confirmation) {
		this.confirmationMessage = confirmation;
	}
	
	/**
	 * Default Constructor.
	 */
	public ApiResponse() {}
	
}
