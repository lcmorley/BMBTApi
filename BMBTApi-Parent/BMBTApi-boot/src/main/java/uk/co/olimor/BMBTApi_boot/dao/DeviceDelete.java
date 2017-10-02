package uk.co.olimor.BMBTApi_boot.dao;

import uk.co.olimor.BMBTApi_Common.exception.ApiException;

/**
 * Delete the device history from the db including users, testResults and the device itself.
 * 
 * @author leonmorley
 *
 */
public interface DeviceDelete {
	
	/**
	 * Given the deviceId, remove the device and all associated data.
	 * 
	 * @param deviceId - the device id.
	 */
	void deleteDevice(String deviceId) throws ApiException;

}
