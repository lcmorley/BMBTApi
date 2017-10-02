package uk.co.olimor.BMBTApi_boot.dao;

import uk.co.olimor.BMBTApi_Common.exception.ApiException;
import uk.co.olimor.BMBTApi_Common.model.DatabaseToken;

public interface TokenCheckQuery {

	/**
	 * Given a device ID, retrieve the token information from the DB.
	 * 
	 * @param deviceId - the device id.
	 * 
	 * @return - the {@link DatabaseToken} from the db.
	 */
	DatabaseToken getTokenFromDeviceId(String deviceId)  throws ApiException; 
	
}
