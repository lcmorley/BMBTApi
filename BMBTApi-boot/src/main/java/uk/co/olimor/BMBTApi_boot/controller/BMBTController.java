package uk.co.olimor.BMBTApi_boot.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.builder.ResultsAnalysisBuilder;
import uk.co.olimor.BMBTApi_boot.common.Constants;
import uk.co.olimor.BMBTApi_boot.dao.DeviceDelete;
import uk.co.olimor.BMBTApi_boot.dao.DeviceExistsQuery;
import uk.co.olimor.BMBTApi_boot.dao.DeviceInsert;
import uk.co.olimor.BMBTApi_boot.dao.DeviceSecurityInsert;
import uk.co.olimor.BMBTApi_boot.dao.DeviceSecurityUpdate;
import uk.co.olimor.BMBTApi_boot.dao.NewUserInsert;
import uk.co.olimor.BMBTApi_boot.dao.ResultHistoryQuery;
import uk.co.olimor.BMBTApi_boot.dao.TestExistsQuery;
import uk.co.olimor.BMBTApi_boot.dao.TestQuery;
import uk.co.olimor.BMBTApi_boot.dao.TestResultInsert;
import uk.co.olimor.BMBTApi_boot.dao.UserQuery;
import uk.co.olimor.BMBTApi_boot.dao.UsersByDeviceIdQuery;
import uk.co.olimor.BMBTApi_boot.exception.ApiException;
import uk.co.olimor.BMBTApi_boot.model.DatabaseToken;
import uk.co.olimor.BMBTApi_boot.model.SuccessLoginResponse;
import uk.co.olimor.BMBTApi_boot.model.TestAnalysis;
import uk.co.olimor.BMBTApi_boot.model.TestResult;
import uk.co.olimor.BMBTApi_boot.model.User;
import uk.co.olimor.BMBTApi_boot.requestmodel.CreateUserRequest;
import uk.co.olimor.BMBTApi_boot.response.ApiResponse;
import uk.co.olimor.BMBTApi_boot.response.ApiResponseError;
import uk.co.olimor.BMBTApi_boot.security.JWTUtil;

@RestController
@Log4j2
public class BMBTController {

	/**
	 * Database Service instance.
	 */
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * {@link TestQuery} instance.
	 */
	@Autowired
	private TestQuery testQuery;
	
	/**
	 * {@link TestExistsQuery} instance.
	 */
	@Autowired
	private TestExistsQuery testExistsQuery;
	
	/**
	 * {@link TestResultInsert} instance.
	 */
	@Autowired
	private TestResultInsert resultInsert;
	
	/**
	 * {@link ResultHistoryQuery} instance.
	 */
	@Autowired
	private ResultHistoryQuery resultHistoryQuery;
	
	/**
	 * {@link UsersByDeviceIdQuery} instance.
	 */
	@Autowired
	private UsersByDeviceIdQuery usersByDeviceIdQuery;
	
	/**
	 * {@link NewUserInsert} instance.
	 */
	@Autowired
	private NewUserInsert newUserInsert;
	
	/**
	 * {@link DeviceInsert} instance
	 */
	@Autowired
	private DeviceInsert deviceInsert;
	
	/**
	 * {@link DeviceDelete} instance.
	 */
	private DeviceDelete deviceDelete;
	
	/**
	 * Query to determine if a device exists.
	 */
	@Autowired
	private DeviceExistsQuery deviceExists;
	
	/**
	 * {@link DeviceSecurityInsert} instance.
	 */
	@Autowired
	private DeviceSecurityInsert deviceSecurityInsert;
	
	/**
	 * {@link DeviceSecurityUpdate} instance.
	 */
	@Autowired
	private DeviceSecurityUpdate deviceSecurityUpdate;
	
	/**
	 * {@link ResultsAnalysisBuilder} instance.
	 */
	@Autowired
	private ResultsAnalysisBuilder resultAnalysisBuilder;
	
	/**
	 * Instance of the JWT Utility.
	 */
	@Autowired
	private JWTUtil jwtUtil;
	
	/**
	 * Method to return a {@link User} object given an id.
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/user/{id}", produces = "application/json")
	public ResponseEntity<ApiResponse> getUser(@PathVariable("id") final String id) {
		log.entry(id);		
		
		try {			
			if (id == null || id.isEmpty()) {
				log.error("An id must be supplied.");
				throw new ApiException("An id MUST be supplied.", HttpStatus.BAD_REQUEST);
			}
			
			return log.traceExit(new ResponseEntity<ApiResponse>(
					new ApiResponse(userQuery.getUser(id)), HttpStatus.OK));
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}
		
	}
	
	/**
	 * Endpoint to retrieve users by a device id.
	 * 
	 * @param id - the id to retrieve by.
	 * 
	 * @return - the Users.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/usersByDeviceId/{id}", produces = "application/json")
	public ResponseEntity<ApiResponse> getUsersByDeviceId(@PathVariable("id") final String id) {
		log.entry(id);		
		
		try {			
			return log.traceExit(new ResponseEntity<ApiResponse>(
					new ApiResponse(usersByDeviceIdQuery.getUsersByDeviceId(id)), HttpStatus.OK));
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}
	}
	
	/**
	 * @return - a list of all possible tests.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/tests", produces = "application/json")
	public ResponseEntity<ApiResponse> getTests() {
		log.traceEntry();
		
		try {
			return log.traceExit(new ResponseEntity<ApiResponse>(
					new ApiResponse(testQuery.getTests()), HttpStatus.OK));
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}
		
	}
	
	/**
	 * API to allow a user to submit a test result from the apps.
	 * 
	 * @param result - the {@link TestResult} object.
	 * 
	 * @return - success message on submission.
	 */
	@RequestMapping(value = "/submitResult", method = RequestMethod.POST, consumes= "application/json")
	public ResponseEntity<ApiResponse> submitResult(@RequestBody final TestResult result) {
		log.entry(result);
		
		final String userId = result.getUserId();
		final int testId = result.getTestId();
		
		try {
			validateSubmitResultRequest(userId, testId);
			
			resultInsert.saveTestResult(result);
			
			return log.traceExit(new ResponseEntity<ApiResponse>(
					new ApiResponse("Test Result submitted successfully"), HttpStatus.OK));	
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}	
	}

	/**
	 * Validate the Submit result request.
	 * 
	 * @param userId - the user id.
	 * @param testId - the test id.
	 * 
	 * @throws ApiException - the exception thrown when validation fails.
	 */
	private void validateSubmitResultRequest(final String userId, final int testId) throws ApiException {
		if (userId == null || userId.isEmpty()) {
			log.error("User ID must be on the request.");
			throw new ApiException("User ID must be on the request.", HttpStatus.BAD_REQUEST);
		}
		
		if (userQuery.getUser(userId) == null) {
			log.error("User not found.");
			throw new ApiException("User not found.", HttpStatus.NOT_FOUND);
		}
		
		if (testId == 0) {
			log.error("Test ID must be on the request.");
			throw new ApiException("Test ID must be on the request.", HttpStatus.BAD_REQUEST);
		}
		
		if (!testExistsQuery.testExists(testId)) {
			log.error("Test not found.");
			throw new ApiException("Test not found.", HttpStatus.NOT_FOUND);	
		}
	}
	
	/**
	 * @return - an analysis of the test results.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/resultsAnalysis/user/{id}", produces = "application/json")
	public ResponseEntity<ApiResponse> getResultsAnalysisByUser(@PathVariable("id") final String userId) {
		log.entry(userId);
		
		try {			
			final List<TestAnalysis> analysis = resultAnalysisBuilder.buildResultsAnalysis(
					resultHistoryQuery.getResultHistory(userId));
			
			return log.traceExit(new ResponseEntity<ApiResponse>(new ApiResponse(analysis), HttpStatus.OK));
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}
	}
	
	/**
	 * @return - an analysis of the test results.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/createUser", consumes = "application/json", 
			produces = "application/json")
	public ResponseEntity<ApiResponse> createUser(@RequestBody final CreateUserRequest user) {
		log.entry(user);
		
		final String deviceId = user.getDeviceId();
		final String userName = user.getUserName();
		
		try {
			validateCreateUserRequest(deviceId, userName);
			
			final String userId = newUserInsert.insertUser(userName, deviceId);
			
			final ApiResponse response = new ApiResponse();
			response.setObject(userId);
			
			return log.traceExit(new ResponseEntity<ApiResponse>(response, HttpStatus.OK));
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}
		
	}

	/**
	 * Validation for the {@link CreateUserRequest} object.
	 * 
	 * @param deviceId - the device id.
	 * @param userName - the user name.
	 * 
	 * @throws ApiException - exception thrown when validation fails.
	 */
	private void validateCreateUserRequest(final String deviceId, final String userName) throws ApiException {
		log.traceEntry(deviceId, userName);
		
		if (userName == null || userName.isEmpty()) {
			log.error("A username MUST be present on the request.");
			throw new ApiException("No username found on the request.", HttpStatus.BAD_REQUEST);
		}
		
		if (deviceId == null || deviceId.isEmpty()) {
			log.error("A device id MUST be present on the request.");
			throw new ApiException("No device id found on the request.", HttpStatus.BAD_REQUEST);
		}
		
		if (!deviceExists.deviceExists(deviceId)) {
			log.error("Device not found with id: " + deviceId);
			throw new ApiException("Device id not found.", HttpStatus.NOT_FOUND);
		}
		
		log.traceExit();
	}
	
	/*
	 * Remove device from the API.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/removeDevice", consumes = "application/json", 
			produces = "application/json")
	public ResponseEntity<ApiResponse> removeDevice(final HttpServletRequest request) {
		final String deviceId = (String) request.getAttribute(Constants.DEVICE);
		
		if (deviceId == null || deviceId.isEmpty()) {
			log.error("Device id is empty.");
			return log.traceExit(buildErrorResponse(new ApiException("Device id is empty.", 
					HttpStatus.UNAUTHORIZED)));
		}
		
		try {
			if (!deviceExists.deviceExists(deviceId)) 
				return log.traceExit(buildErrorResponse(new ApiException("Device not found.", 
						HttpStatus.NOT_FOUND)));

			deviceDelete.deleteDevice(deviceId);
			
			return log.traceExit(new ResponseEntity<ApiResponse>(new ApiResponse(), HttpStatus.OK));				
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}				
	}

	/*
	 * Register device with the API.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/registerDevice", consumes = "application/json", 
			produces = "application/json")
	public ResponseEntity<ApiResponse> registerDevice(final HttpServletRequest request) {
		final String deviceId = (String) request.getAttribute(Constants.DEVICE);
		
		if (deviceId == null || deviceId.isEmpty()) {
			log.error("Device id is empty.");
			return log.traceExit(buildErrorResponse(new ApiException("Device id is empty.", 
					HttpStatus.UNAUTHORIZED)));
		}
		
		try {
			if (deviceExists.deviceExists(deviceId)) 
				return log.traceExit(buildErrorResponse(new ApiException("Device already exists.", 
						HttpStatus.BAD_REQUEST)));

			if (deviceInsert.insertDevice(deviceId) == Constants.INT_ZERO) 
				return log.traceExit(buildErrorResponse(new ApiException("Unable to register the device.", 
						HttpStatus.INTERNAL_SERVER_ERROR)));
			
			if (deviceSecurityInsert.insertIntoDeviceSecurity(deviceId) == Constants.INT_ZERO) 
				return log.traceExit(buildErrorResponse(new ApiException("Unable to create device security record.", 
						HttpStatus.INTERNAL_SERVER_ERROR)));
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}
		
		return log.traceExit(new ResponseEntity<ApiResponse>(new ApiResponse(), HttpStatus.OK));
	}
	
	/**
	 * Attempt to log into the API.
	 * 
	 * @param request - the request containing the data.
	 * 
	 * @return - the generated JWT token.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/login", consumes = "application/json", 
			produces = "application/json")
	public ResponseEntity<ApiResponse> login(final HttpServletRequest request) {
		log.traceEntry();
		
		final String deviceId = (String) request.getAttribute(Constants.DEVICE);
		
		if (deviceId == null || deviceId.isEmpty()) {
			log.error("Device id is empty.");
			return log.traceExit(buildErrorResponse(new ApiException("Invalid login attempt.", 
					HttpStatus.UNAUTHORIZED)));
		}
			
		try {
			if (!deviceExists.deviceExists(deviceId)) {
				log.error("Invalid device id. Not found on the DB: " + deviceId);
				return log.traceExit(buildErrorResponse(new ApiException("Invalid login attempt.", 
						HttpStatus.UNAUTHORIZED)));
			}
			
			final String token = jwtUtil.generateJWTToken(deviceId);
			
			final DatabaseToken dbToken = new DatabaseToken();
			dbToken.setDeviceId(deviceId);
			dbToken.setToken(token);
	
			if (deviceSecurityUpdate.updateDeviceSecurity(dbToken) == Constants.INT_ZERO) 
				return log.traceExit(buildErrorResponse(new ApiException("Unable to update device security record.", 
						HttpStatus.INTERNAL_SERVER_ERROR)));
			
			final ApiResponse response = new ApiResponse();
			response.setObject(new SuccessLoginResponse(token));
			
			return log.traceExit(new ResponseEntity<ApiResponse>(response, HttpStatus.OK));
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}
	}
	
	/**
	 * Build and return a {@link ResponseEntity} with an error response.
	 * 
	 * @param e - the exception raised.
	 * 
	 * @return - the build error response.
	 */
	private ResponseEntity<ApiResponse> buildErrorResponse(final ApiException e) {
		return log.traceExit(new ResponseEntity<ApiResponse>(
				new ApiResponse(null, new ApiResponseError(e)), e.getStatus()));
	}
	
}
