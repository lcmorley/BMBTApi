package uk.co.olimor.BMBTApi_boot.dao;

import uk.co.olimor.BMBTApi_Common.exception.ApiException;
import uk.co.olimor.BMBTApi_Common.model.User;

/**
 * 
 * @author leonmorley
 *
 */
public interface UserQuery {

	/**
	 * @param userid - the userid.
	 * 
	 * @return - a {@link User} object.
	 */
	User getUser(String userId) throws ApiException;
	
}
