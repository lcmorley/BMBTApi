package uk.co.olimor.BMBTApi_boot.model;

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
	private int id;
	
	/**
	 * First Name.
	 */
	private String firstName;

}
