package uk.co.olimor.BMBTApi_Common.requestmodel;

import lombok.Data;

/**
 * Request object for the CreateUser object.
 * 
 * @author leonmorley
 *
 */
@Data
public class CreateUserRequest {

	/**
	 * User name value.
	 */
	private String userName;
	
	/**
	 * DeviceId value.
	 */
	private String deviceId;
	
}
