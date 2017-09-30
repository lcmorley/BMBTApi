package uk.co.olimor.BMBTApi_boot.model;

import lombok.Data;

/**
 * Class which holds login credential information.
 * 
 * @author leonmorley
 *
 */
@Data
public class LoginCredentials {

	/**
	 * User id.
	 */
	private String userId;
	
	/**
	 * Password.
	 */
	private String password;
	
	/**
	 * The device id logging in.
	 */
	private String deviceId;
	
}
