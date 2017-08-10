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
import uk.co.olimor.BMBTApi_boot.dao.TestQuery;
import uk.co.olimor.BMBTApi_boot.dao.UserQuery;
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
	 * Method to return a {@link User} object given an id.
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "user/{id}", produces = "application/json")
	public ResponseEntity<User> getUser(@PathVariable("id") final Integer id) {
		log.entry(id);		
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
		return log.traceExit(new ResponseEntity<String>("Test submitted successfully", HttpStatus.OK));
	}

}
