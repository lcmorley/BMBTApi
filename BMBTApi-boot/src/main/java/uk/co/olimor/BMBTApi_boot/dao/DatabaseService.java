package uk.co.olimor.BMBTApi_boot.dao;

import java.util.List;

import uk.co.olimor.BMBTApi_boot.model.User;

/**
 * 
 * @author leonmorley
 *
 */
public interface DatabaseService {

	/**
	 * @return - a {@link List} or {@link User} objects.
	 */
	List<User> getUsers();
	
}
