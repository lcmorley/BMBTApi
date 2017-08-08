package uk.co.olimor.BMBTApi_boot.dao;

import uk.co.olimor.BMBTApi_boot.model.User;

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
	User getUser(int userId);
	
}
