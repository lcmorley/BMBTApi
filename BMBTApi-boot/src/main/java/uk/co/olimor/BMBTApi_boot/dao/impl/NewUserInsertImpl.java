package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.NewUserInsert;
import uk.co.olimor.BMBTApi_boot.exception.ApiException;
import uk.co.olimor.BMBTApi_boot.model.User;

@Log4j2
@Service
public class NewUserInsertImpl extends AbstractInsert<User> implements NewUserInsert {

	/**
	 * Insert statement with placeholders.
	 */
	private static final String INSERT_STATEMENT = "INSERT INTO users VALUES ('%s', '%s')";

	@Override
	public String insertUser(String userName) throws ApiException {
		log.entry(userName);

		final String userId = UUID.randomUUID().toString();
		final User user = new User(userId, userName);
		final int result = insert(user);

		if (result == 0)
			logError(log, "The user was not inserted into the db.", null, HttpStatus.INTERNAL_SERVER_ERROR);

		return log.traceExit(userId);
	}

	@Override
	protected String buildInsert(User user) {
		log.entry(user);
		return log.traceExit(String.format(INSERT_STATEMENT, user.getId(), user.getFirstName()));
	}

}
