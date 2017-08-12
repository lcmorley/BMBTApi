package uk.co.olimor.BMBTApi_boot.controller;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.Application;
import uk.co.olimor.BMBTApi_boot.model.User;

/**
 * This test client was written to provide an integration test for the deployed
 * receipt service.
 * 
 * @author morlel
 *
 */
@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BMBTControllerTest {

	/**
	 * The local server port to use in the test.
	 */
	@LocalServerPort
	private int port;

	/**
	 * {@link TestRestTemplate} instance.
	 */
	final TestRestTemplate restTemplate = new TestRestTemplate();

	/**
	 * Test happy path (/user/1).
	 * 
	 * @throws JSONException
	 */
	@Test
	public void test_User_Happy() throws JSONException {
		log.traceEntry();
		ResponseEntity<User> response = restTemplate.getForEntity(createURLWithPort("/user/1"), User.class);
		final User expectedUser = new User(1, "Oliver AWS");

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(expectedUser, response.getBody());
		
		log.traceExit();
	}

	/**
	 * Test unknown user (/user/100).
	 * 
	 * @throws JSONException
	 */
	@Test
	public void test_User_Unknown() throws JSONException {
		log.traceEntry();
		ResponseEntity<User> response = restTemplate.getForEntity(createURLWithPort("/user/100"), User.class);
		
		Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		Assert.assertNull(response.getBody());
		
		log.traceExit();
	}
		
	/**
	 * Create URL with port.
	 * 
	 * @param uri - the endpoint to call.
	 * 
	 * @return - the full uri to call.
	 */
	private String createURLWithPort(String uri) {
		log.traceEntry();
		return log.traceExit("http://localhost:" + port + uri);
	}

}
