package uk.co.olimor.BMBTApi_boot.dao;

import uk.co.olimor.BMBTApi_boot.exception.ApiException;
import uk.co.olimor.BMBTApi_boot.model.DatabaseToken;

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
	 * 
	 * @return - the number of rows updated.
	 */
	int updateDeviceSecurity(final DatabaseToken token) throws ApiException;
	
}
