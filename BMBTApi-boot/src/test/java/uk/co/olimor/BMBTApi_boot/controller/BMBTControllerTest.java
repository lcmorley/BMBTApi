package uk.co.olimor.BMBTApi_boot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.Application;
import uk.co.olimor.BMBTApi_boot.model.Question;
import uk.co.olimor.BMBTApi_boot.model.ResultsAnalysis;
import uk.co.olimor.BMBTApi_boot.model.TestResult;
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
	 * Test happy path (/user/1).
	 * 
	 * @throws JSONException
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Test
	public void test_Tests_Happy() throws JSONException, JsonParseException, JsonMappingException, IOException {
		log.traceEntry();
		ResponseEntity<uk.co.olimor.BMBTApi_boot.model.Test[]> response = restTemplate.getForEntity(createURLWithPort("/tests"), 
					uk.co.olimor.BMBTApi_boot.model.Test[].class);
		
		final Question question = new Question(4, 4, "+");
		final List<Question> questions = new ArrayList<>();
		questions.add(question);
		
		final uk.co.olimor.BMBTApi_boot.model.Test expected = new uk.co.olimor.BMBTApi_boot.model.Test(1, 
				"Reception", 20, questions);
		
		List<uk.co.olimor.BMBTApi_boot.model.Test> tests = Arrays.asList(response.getBody());
		
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(1, tests.size());	
		Assert.assertEquals(expected, tests.get(0));
		
		log.traceExit();
	}

	@Test
	public void testSubmitResult()  {
		log.traceEntry();
		
		final TestResult result = new TestResult(1, 1, 5, 2, 5, 10.5f);
		 
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", "application/json");       
        HttpEntity<TestResult> entity = new HttpEntity<TestResult>(result,headers);
        
        final ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/submitResult"), 
        		entity, String.class); 
        
        Assert.assertEquals("Test Result submitted successfully", response.getBody());
        
		log.traceExit();
	}
		
	/**
	 * Test test results analysis happy path (/resultHistory/1).
	 * 
	 * @throws JSONException
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Test
	public void test_ResultAnalysis_Happy() throws JSONException, JsonParseException, JsonMappingException, IOException {
		log.traceEntry();
		
		final ResponseEntity<ResultsAnalysis> response = restTemplate.getForEntity(createURLWithPort("/resultsAnalysis/1"), 
					ResultsAnalysis.class);

		final ResultsAnalysis expected = new ResultsAnalysis();

		expected.setTotalTests(1);
		expected.setAverageAttemptedQuestions(5);
		expected.setAverageCorrectAnswers(5);
		expected.setTopCorrectAnswers(5);
		expected.setAverageTime(10.5f);
		expected.setBestTime(10.5f);
		
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(expected, response.getBody());
		
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
