package uk.co.olimor.lambda.user_service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.exception.ApiException;
import uk.co.olimor.BMBTApi_Common.model.User;
import uk.co.olimor.BMBTApi_Common.requestmodel.ApiRequest;
import uk.co.olimor.BMBTApi_Common.requestmodel.CreateUserRequest;
import uk.co.olimor.BMBTApi_Common.response.ApiResponse;
import uk.co.olimor.BMBTApi_Common.response.ApiResponseError;
import uk.co.olimor.lambda.user_service.dao.NewUserInsert;
import uk.co.olimor.lambda.user_service.dao.UserQuery;
import uk.co.olimor.lambda.user_service.dao.UsersByDeviceIdQuery;
import uk.co.olimor.lambda.user_service.dao.impl.NewUserInsertImpl;
import uk.co.olimor.lambda.user_service.dao.impl.UserQueryImpl;
import uk.co.olimor.lambda.user_service.dao.impl.UsersByDeviceIdQueryimpl;
import uk.co.olimor.lambda.user_service.utility.PropertyUtil;

@Log4j2
public class UserRequestHandler implements RequestHandler<ApiRequest, ResponseEntity<ApiResponse>> {

	/**
	 * DAO used to create a user.
	 */
	private NewUserInsert userInsertDAO;

	/**
	 * DAO used to retrieve a User.
	 */
	private UserQuery userQueryDAO;

	/**
	 * DAO used to get users for a specific device.
	 */
	private UsersByDeviceIdQuery userByDeviceIdDAO;

	/**
	 * Construct object.
	 */
	public UserRequestHandler() {
		setupDAOs(getDataSource());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<ApiResponse> handleRequest(ApiRequest input, Context context) {
		log.entry(input);

		try {
			switch (input.getEndpointName()) {
			case "createUser":
				return log.traceExit(
						createUser(deserialiseToCreateUserRequest((HashMap<String, String>) input.getInput())));
			case "getUser":
				return log.traceExit(getUser((String) input.getInput()));
			case "getUserByDeviceId":
				return log.traceExit(getUsersByDeviceId((String) input.getInput()));
			}
		} catch (final IOException e) {
			log.error("An exception occurred attempting to convert object.", e);
			return log.traceExit(buildErrorResponse(new ApiException(
					"An exception occurred attempting to " + "convert object.", e, HttpStatus.INTERNAL_SERVER_ERROR)));
		}

		return log.traceExit(new ResponseEntity<ApiResponse>(new ApiResponse(), HttpStatus.BAD_REQUEST));
	}

	public ResponseEntity<ApiResponse> createUser(final CreateUserRequest user) {
		log.entry(user);

		final String deviceId = user.getDeviceId();
		final String userName = user.getUserName();

		try {
			validateCreateUserRequest(deviceId, userName);

			final String userId = userInsertDAO.insertUser(userName, deviceId);

			final ApiResponse response = new ApiResponse();
			response.setObject(userId);

			return log.traceExit(new ResponseEntity<ApiResponse>(response, HttpStatus.OK));
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}
	}

	public ResponseEntity<ApiResponse> getUser(final String id) {
		log.traceEntry(id);

		try {
			if (id == null || id.isEmpty()) {
				log.error("An id must be supplied.");
				throw new ApiException("An id MUST be supplied.", HttpStatus.BAD_REQUEST);
			}

			return log.traceExit(
					new ResponseEntity<ApiResponse>(new ApiResponse(userQueryDAO.getUser(id)), HttpStatus.OK));
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}
	}

	/**
	 * Endpoint to retrieve users by a device id.
	 * 
	 * @param id
	 *            - the id to retrieve by.
	 * 
	 * @return - the Users.
	 */
	public ResponseEntity<ApiResponse> getUsersByDeviceId(final String id) {
		log.traceEntry(id);

		try {
			return log.traceExit(new ResponseEntity<ApiResponse>(
					new ApiResponse(userByDeviceIdDAO.getUsersByDeviceId(id)), HttpStatus.OK));
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}
	}

	/**
	 * Validation for the {@link CreateUserRequest} object.
	 * 
	 * @param deviceId
	 *            - the device id.
	 * @param userName
	 *            - the user name.
	 * 
	 * @throws ApiException
	 *             - exception thrown when validation fails.
	 */
	private void validateCreateUserRequest(final String deviceId, final String userName) throws ApiException {
		log.traceEntry(deviceId + userName);

		if (userName == null || userName.isEmpty()) {
			log.traceExit("A username MUST be present on the request.");
			throw new ApiException("No username found on the request.", HttpStatus.BAD_REQUEST);
		}

		if (deviceId == null || deviceId.isEmpty()) {
			log.traceExit("A device id MUST be present on the request.");
			throw new ApiException("No device id found on the request.", HttpStatus.BAD_REQUEST);
		}

		// if (!deviceExists.deviceExists(deviceId)) {
		// log.log("Device not found with id: " + deviceId);
		// throw new ApiException("Device id not found.", HttpStatus.NOT_FOUND);
		// }

		log.traceExit("Exit");
	}

	/**
	 * Setup the DAO objects and inject the datasources.
	 * 
	 * @param datasource
	 *            - the datasource.
	 */
	private void setupDAOs(final DataSource datasource) {
		log.traceEntry();

		userInsertDAO = new NewUserInsertImpl(datasource);
		userQueryDAO = new UserQueryImpl(datasource);
		userByDeviceIdDAO = new UsersByDeviceIdQueryimpl(datasource);

		log.traceExit();
	}

	/**
	 * Database {@link DataSource} bean configuration.
	 * 
	 * @return the datasource.
	 */
	public DataSource getDataSource() {
		log.traceEntry();

		final MysqlDataSource dataSource = new MysqlDataSource();

		dataSource.setUser(PropertyUtil.getProperty("userName"));
		dataSource.setPassword(PropertyUtil.getProperty("password"));
		dataSource.setServerName(PropertyUtil.getProperty("connectionString"));
		dataSource.setDatabaseName(PropertyUtil.getProperty("schema"));

		return log.traceExit(dataSource);
	}

	/**
	 * Build and return a {@link ResponseEntity} with an error response.
	 * 
	 * @param e
	 *            - the exception raised.
	 * 
	 * @return - the build error response.
	 */
	private ResponseEntity<ApiResponse> buildErrorResponse(final ApiException e) {
		return log.traceExit(
				new ResponseEntity<ApiResponse>(new ApiResponse(null, new ApiResponseError(e)), e.getStatus()));
	}

	/**
	 * Convert from a JSON string to an object.
	 * 
	 * @param json
	 *            - the object to deserialize.
	 *
	 * @return - the {@link User} object.
	 *
	 * @throws IOException
	 *             - the exception thrown.
	 */
	public CreateUserRequest deserialiseToCreateUserRequest(final Map<String, String> json) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		return log.traceExit((CreateUserRequest) mapper.convertValue(json, new TypeReference<CreateUserRequest>() {
		}));
	}

}
