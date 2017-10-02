package uk.co.olimor.BMBTApi_Common.model;

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
