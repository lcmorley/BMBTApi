package uk.co.olimor.BMBTApi_boot.dao;

import uk.co.olimor.BMBTApi_Common.exception.ApiException;

/**
 * Interface for the Insert Device query.
 * 
 * @author leonmorley
 *
 */
public interface DeviceInsert {

	/**
	 * Device inserted into the DB.
	 * 
	 * @param deviceId - the device to insert.
	 */
	void insertDevice(String deviceId) throws ApiException ;
	
}
