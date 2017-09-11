package uk.co.olimor.BMBTApi_boot.dao;

import java.util.List;

import uk.co.olimor.BMBTApi_boot.exception.ApiException;
import uk.co.olimor.BMBTApi_boot.model.User;

/**
 * Query interface to retrieve {@link User} objects by device id.
 * 
 * @author leonmorley
 *
 */
public interface UsersByDeviceIdQuery {

	/**
	 * Get users by a device id.
	 * 
	 * @param deviceId - the device id.
	 * 
	 * @return a {@link List} of {@link User} objects.
	 * 
	 * @throws ApiException - exception thrown.
	 */
	List<User> getUsersByDeviceId(final String deviceId) throws ApiException;
	
}
