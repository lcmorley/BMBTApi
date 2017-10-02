package uk.co.olimor.BMBTApi_boot.dao;

import uk.co.olimor.BMBTApi_Common.exception.ApiException;

/**
 * Query to Device Exists.
 * 
 * @author leonmorley
 *
 */
public interface DeviceExistsQuery {

	/**
	 * Method to determine if the deviceId exists on the db.
	 * 
	 * @param deviceId - the deviceId.
	 * 
	 * @return true if it exists, otherwise false.
	 * 
	 * @throws ApiException - Exception thrown when the query fails.
	 */
	boolean deviceExists(String deviceId) throws ApiException;
	
}
