package uk.co.olimor.BMBTApi_Common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User object.
 * 
 * @author leonmorley
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	/**
	 * Id value.
	 */
	private String id;
	
	/**
	 * Device Id.
	 */
	private String deviceId;
	
	/**
	 * First Name.
	 */
	private String firstName;

}
