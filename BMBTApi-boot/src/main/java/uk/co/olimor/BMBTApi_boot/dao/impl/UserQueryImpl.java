package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.UserQuery;
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
	public User getUser(int userId) {
		log.traceEntry();
		
		final List<User> users = query("SELECT * FROM users where id=" + userId);	
		
		if (users.size() == 0) { 
			log.error("Unable to find user with id: " + userId);
			return null;
		}
		
		return log.traceExit(users.get(0));
	}

	@Override
	protected List<User> buildResult(final ResultSet result) {
		log.entry(result);

		final List<User> users = new ArrayList<>();

		try {
			while (result.next())
				users.add(new User(result.getInt(1), result.getString(2), "lcmorley"));
		} catch (SQLException e) {
			log.error("An error occurred whilst attempting to build the results.", e);
		}

		return log.traceExit(users);
	}

}
