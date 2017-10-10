package com.amazonaws.lambda.user_service.dao.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.amazonaws.lambda.user_service.dao.NewUserInsert;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.dao.impl.AbstractUpdate;
import uk.co.olimor.BMBTApi_Common.exception.ApiException;
import uk.co.olimor.BMBTApi_Common.model.User;

@Log4j2
@Service
public class NewUserInsertImpl extends AbstractUpdate<User> implements NewUserInsert {

	/**
	 * Insert statement with placeholders.
	 */
	private static final String INSERT_STATEMENT = "INSERT INTO users VALUES ('%s', '%s', '%s')";

	@Override
	public String insertUser(final String userName, final String deviceId) throws ApiException {
		log.entry(userName);
		
		final String userId = generateUniqueUserId();
		final User user = new User(userId, deviceId, userName);
		
		update(user);

		return log.traceExit(userId);
	}

	/**
	 * @return - a unique UUID for the user id.
	 */
	private String generateUniqueUserId() {
		log.traceEntry();
		return log.traceExit(UUID.randomUUID().toString());
	}
	
	@Override
	protected String buildUpdate(User user) {
		log.entry(user);
		return log.traceExit(String.format(INSERT_STATEMENT, user.getId(), user.getDeviceId(), user.getFirstName()));
	}

}
