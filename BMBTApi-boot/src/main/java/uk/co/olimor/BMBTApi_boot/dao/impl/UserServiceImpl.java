package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.UserService;
import uk.co.olimor.BMBTApi_boot.model.Question;
import uk.co.olimor.BMBTApi_boot.model.Test;
import uk.co.olimor.BMBTApi_boot.model.User;

/**
 * Service which implements performs Database CRUD operation.
 * 
 * @author leonmorley
 *
 */
@Service
@Log4j2
public class UserServiceImpl extends AbstractService<User> implements UserService {

	/**
	 * Get users.
	 */
	public List<User> getUsers() {
		log.traceEntry();
		return log.traceExit(query("SELECT * FROM users"));
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
