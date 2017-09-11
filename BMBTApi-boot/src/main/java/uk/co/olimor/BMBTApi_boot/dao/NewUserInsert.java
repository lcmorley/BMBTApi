package uk.co.olimor.BMBTApi_boot.dao;

import uk.co.olimor.BMBTApi_boot.exception.ApiException;

/**
 * Contract for the NewUserInsertImpl class.
 * 
 * @author leonmorley
 *
 */
public interface NewUserInsert {

	/**
	 * Insert a user into the db.
	 * 
	 * @param userName - the user name.
	 * @param deviceId - the id of the device.
	 * 
	 * @return - the new user id.
	 * 
	 * @throws ApiException - exception thrown when a failure occurs.
	 */
	String insertUser(final String userName, final String deviceId) throws ApiException;
	
}
