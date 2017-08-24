package uk.co.olimor.BMBTApi_boot.controller;

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
import uk.co.olimor.BMBTApi_boot.dao.NewUserInsert;
import uk.co.olimor.BMBTApi_boot.dao.ResultHistoryQuery;
import uk.co.olimor.BMBTApi_boot.dao.TestQuery;
import uk.co.olimor.BMBTApi_boot.dao.TestResultInsert;
import uk.co.olimor.BMBTApi_boot.dao.UserQuery;
import uk.co.olimor.BMBTApi_boot.exception.ApiException;
import uk.co.olimor.BMBTApi_boot.model.ResultsAnalysis;
import uk.co.olimor.BMBTApi_boot.model.TestResult;
import uk.co.olimor.BMBTApi_boot.model.User;
import uk.co.olimor.BMBTApi_boot.response.ApiResponse;
import uk.co.olimor.BMBTApi_boot.response.ApiResponseError;

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
	 * {@link NewUserInsert} instance.
	 */
	@Autowired
	private NewUserInsert newUserInsert;
	
	/**
	 * {@link ResultsAnalysisBuilder} instance.
	 */
	@Autowired
	private ResultsAnalysisBuilder resultAnalysisBuilder;
	
	/**
	 * Method to return a {@link User} object given an id.
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "user/{id}", produces = "application/json")
	public ResponseEntity<ApiResponse> getUser(@PathVariable("id") final Integer id) {
		log.entry(id);		
		
		try {			
			return log.traceExit(new ResponseEntity<ApiResponse>(
					new ApiResponse(userQuery.getUser(id)), HttpStatus.OK));
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}
		
	}
	
	/**
	 * @return - a list of all possible tests.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "tests", produces = "application/json")
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
	@RequestMapping(value = "submitResult", method = RequestMethod.POST, consumes= "application/json")
	public ResponseEntity<ApiResponse> submitResult(@RequestBody final TestResult result) {
		log.entry(result);
		
		try {
			resultInsert.saveTestResult(result);
			
			return log.traceExit(new ResponseEntity<ApiResponse>(
					new ApiResponse("Test Result submitted successfully"), HttpStatus.OK));	
		} catch (final ApiException e) {
			return log.traceExit(buildErrorResponse(e));
		}	
	}
	
	/**
	 * @return - an analysis of the test results.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/resultsAnalysis/{id}", produces = "application/json")
	public ResponseEntity<ApiResponse> getResultsAnalysis(@PathVariable("id") final Integer userId) {
		log.entry(userId);
		
		try {			
			final ResultsAnalysis analysis = resultAnalysisBuilder.buildResultsAnalysis(
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
	public ResponseEntity<ApiResponse> createUser(@RequestBody final String userName) {
		log.entry(userName);
		
		try {
			final String userId = newUserInsert.insertUser(userName);
			
			final ApiResponse response = new ApiResponse();
			response.setObject(userId);
			
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
