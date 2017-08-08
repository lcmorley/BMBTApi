package uk.co.olimor.BMBTApi_boot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * User object.
 * 
 * @author leonmorley
 *
 */
@Data
@AllArgsConstructor
public class User {

	/**
	 * Id value.
	 */
	private int id;
	
	/**
	 * First Name.
	 */
	private String firstName;
	
	/**
	 * User name.
	 */
	private String username;
	
}
