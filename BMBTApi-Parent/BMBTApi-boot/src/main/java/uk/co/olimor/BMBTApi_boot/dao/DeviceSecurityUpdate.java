package uk.co.olimor.BMBTApi_boot.dao;

import uk.co.olimor.BMBTApi_Common.exception.ApiException;
import uk.co.olimor.BMBTApi_Common.model.DatabaseToken;

/**
 * Interface to update the deviceSecurity table.
 * 
 * @author leonmorley
 *
 */
public interface DeviceSecurityUpdate {

	/**
	 * Method which updates the securityToken table.
	 * 
	 * @param token - the token to update.
	 */
	void updateDeviceSecurity(final DatabaseToken token) throws ApiException;
	
}
