package uk.co.olimor.BMBTApi_boot.controller;

import java.util.List;

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
import uk.co.olimor.BMBTApi_boot.dao.ResultHistoryQuery;
import uk.co.olimor.BMBTApi_boot.dao.TestQuery;
import uk.co.olimor.BMBTApi_boot.dao.TestResultInsert;
import uk.co.olimor.BMBTApi_boot.dao.UserQuery;
import uk.co.olimor.BMBTApi_boot.model.ResultsAnalysis;
import uk.co.olimor.BMBTApi_boot.model.Test;
import uk.co.olimor.BMBTApi_boot.model.TestResult;
import uk.co.olimor.BMBTApi_boot.model.User;

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
	public ResponseEntity<User> getUser(@PathVariable("id") final Integer id) {
		log.entry(id);		
		final User user = userQuery.getUser(id);
		
		if (user == null)
			return log.traceExit(new ResponseEntity<User>(user, HttpStatus.NOT_FOUND));
		
		return log.traceExit(new ResponseEntity<User>(userQuery.getUser(id), HttpStatus.OK));
	}
	
	/**
	 * @return - a list of all possible tests.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "tests", produces = "application/json")
	public ResponseEntity<List<Test>> getTests() {
		log.traceEntry();
		List<Test> tests = testQuery.getTests();
		return log.traceExit(new ResponseEntity<List<Test>>(tests, HttpStatus.OK));
	}
	
	/**
	 * API to allow a user to submit a test result from the apps.
	 * 
	 * @param result - the {@link TestResult} object.
	 * 
	 * @return - success message on submission.
	 */
	@RequestMapping(value = "submitResult", method = RequestMethod.POST, consumes= "application/json")
	public ResponseEntity<String> submitResult(@RequestBody TestResult result) {
		log.entry(result);
		
		if (resultInsert.saveTestResult(result) == 1) 
			return log.traceExit(new ResponseEntity<String>("Test Result submitted successfully", HttpStatus.OK));	
		else
			return log.traceExit(new ResponseEntity<String>("Test Result submission failed.", HttpStatus.OK));	
	}
	
	/**
	 * @return - an analysis of the test results.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/resultsAnalysis/{id}", produces = "application/json")
	public ResponseEntity<ResultsAnalysis> getResultsAnalysis(@PathVariable("id") final Integer userId) {
		log.entry(userId);
		
		final List<TestResult> resultHistory = resultHistoryQuery.getResultHistory(userId);
		
		final ResultsAnalysis analysis = resultAnalysisBuilder.buildResultsAnalysis(resultHistory);
		
		return log.traceExit(new ResponseEntity<ResultsAnalysis>(analysis, HttpStatus.OK));
	}

}
