package uk.co.olimor.BMBTApi_Common.requestmodel;

import lombok.Data;

/**
 * Api Request class.
 * 
 * @author leonmorley
 *
 */
@Data
public class ApiRequest {

	/**
	 * The name of the endpoint called.
	 */
	private String endpointName;
	
	/**
	 * The object to serialize.
	 */
	private Object input;
	
}
