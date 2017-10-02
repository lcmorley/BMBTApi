package uk.co.olimor.BMBTApi_Common.model;


import java.util.Date;

import lombok.Data;

@Data
public class DatabaseToken {

	/**
	 * The device id.
	 */
	private String deviceId;
	
	/**
	 * The token associated with the device id.
	 */
	private String token;
	
	/**
	 * Expiry date value.
	 */
	private Date expiryDate;
	
}
