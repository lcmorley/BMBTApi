package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.UserQuery;
import uk.co.olimor.BMBTApi_boot.exception.ApiException;
import uk.co.olimor.BMBTApi_boot.model.User;

/**
 * Service which implements performs Database CRUD operation.
 * 
 * @author leonmorley
 *
 */
@Service
@Log4j2
public class UserQueryImpl extends AbstractQuery<User> implements UserQuery {

	/**
	 * Get users.
	 */
	public User getUser(int userId) throws ApiException {
		log.traceEntry();
		
		final List<User> users = query("SELECT * FROM users where id=" + userId);	
		
		if (users.size() == 0) 
			logError(log, "Unable to find user with id: " + userId, HttpStatus.NOT_FOUND);
		
		return log.traceExit(users.get(0));
	}

	@Override
	protected List<User> buildResult(final ResultSet result) throws ApiException {
		log.entry(result);

		final List<User> users = new ArrayList<>();

		try {
			while (result.next())
				users.add(new User(result.getInt(1), result.getString(2)));
		} catch (final SQLException e) {
			logError(log, "An error occurred whilst attempting to build the results.", e, 
					HttpStatus.INTERNAL_SERVER_ERROR);			
		}

		return log.traceExit(users);
	}

}
