package uk.co.olimor.BMBTApi_boot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.co.olimor.BMBTApi_boot.dao.DatabaseService;
import uk.co.olimor.BMBTApi_boot.model.User;

@RestController
public class BMBTController {

	/**
	 * Database Service instance.
	 */
	@Autowired
	private DatabaseService service;
	
	/**
	 * @param userId
	 * @return a test history for the users.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "history/{id}", produces = "application/json")
	public ResponseEntity<String> getTestHistory(@PathVariable("id") final String userId) {
		final String historyJson = "{\"history\" : {\"id\":\"" + userId + "1234567890\"}}";		
		return new ResponseEntity<String>(historyJson, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "users", produces = "application/json")
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = service.getUsers();
		System.out.println(users);
		return new ResponseEntity(users, HttpStatus.OK);
	}
	
}
