package uk.co.olimor.BMBTApi_boot.dao;

import uk.co.olimor.BMBTApi_boot.exception.ApiException;

/**
 * Interface to Insert into deviceSecurity.
 * 
 * @author leonmorley
 *
 */
public interface DeviceSecurityInsert {

	/**
	 * Insert deviceId into the deviceSecurity table.
	 * 
	 * @param deviceId - the deviceId to insert.
	 */
	int insertIntoDeviceSecurity(final String deviceId) throws ApiException;
	
}
