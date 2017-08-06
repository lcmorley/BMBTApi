package uk.co.olimor.BMBTApi_boot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BMBTController {

	@RequestMapping(method = RequestMethod.GET, value = "history/{id}", produces = "application/json")
	public ResponseEntity<String> getTestHistory(@PathVariable("id") final String userId) {
		final String historyJson = "{\"history\" : {\"id\":\"" + userId + "1234567890\"}}";		
		return new ResponseEntity<String>(historyJson, HttpStatus.OK);
	}
	
}
