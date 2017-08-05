package uk.co.olimor.BMBTApi_boot.controller;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class BMBTControllerTest {

	@Test
	public void testGetTestHistory() {
		final String expectedJson = "{\"history\" : {\"id\":\"Leon12345\"}}";
		final ResponseEntity<String> expected = new ResponseEntity<String>(expectedJson, HttpStatus.OK);
		
		Assert.assertEquals(expected, new BMBTController().getTestHistory("Leon"));
	}
	
}
