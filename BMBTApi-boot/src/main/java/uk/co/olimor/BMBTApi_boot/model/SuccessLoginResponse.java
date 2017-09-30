package uk.co.olimor.BMBTApi_boot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Successful login response.
 * 
 * @author leonmorley
 *
 */
@Data
@AllArgsConstructor
public class SuccessLoginResponse {

	/**
	 * Response token.
	 */
	private String token;
	
}
