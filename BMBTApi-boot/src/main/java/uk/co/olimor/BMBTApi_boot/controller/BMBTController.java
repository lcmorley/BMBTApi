package uk.co.olimor.BMBTApi_boot.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.TestQuery;
import uk.co.olimor.BMBTApi_boot.dao.UserQuery;
import uk.co.olimor.BMBTApi_boot.dao.impl.TestQueryImpl;
import uk.co.olimor.BMBTApi_boot.model.Test;
import uk.co.olimor.BMBTApi_boot.model.User;

@RestController
@Log4j2
public class BMBTController {

	/**
	 * Database Service instance.
	 */
	@Autowired
	private UserQuery service;
	
	/**
	 * {@link TestQuery} instance.
	 */
	@Autowired
	private TestQueryImpl testService;
	
	@RequestMapping(method = RequestMethod.GET, value = "user/{id}", produces = "application/json")
	public ResponseEntity<User> getUser(@PathVariable("id") final Integer id) {
		log.entry(id);		
		return new ResponseEntity<User>(service.getUser(id), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "tests", produces = "application/json")
	public ResponseEntity<List<Test>> getTests() {
		List<Test> tests = testService.getTests();
		return new ResponseEntity<List<Test>>(tests, HttpStatus.OK);
	}
}
