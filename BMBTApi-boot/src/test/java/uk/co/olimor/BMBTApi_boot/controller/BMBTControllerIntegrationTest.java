package uk.co.olimor.BMBTApi_boot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import uk.co.olimor.BMBTApi_boot.JsonDeserialiser;
import uk.co.olimor.BMBTApi_boot.model.Question;
import uk.co.olimor.BMBTApi_boot.model.ResultsAnalysis;
import uk.co.olimor.BMBTApi_boot.model.TestResult;
import uk.co.olimor.BMBTApi_boot.model.User;
import uk.co.olimor.BMBTApi_boot.requestmodel.CreateUserRequest;
import uk.co.olimor.BMBTApi_boot.response.ApiResponse;

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
public class BMBTControllerIntegrationTest {

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
	 * Instance of the {@link JsonDeserialiser}.
	 */
	final JsonDeserialiser deserialiser = new JsonDeserialiser();
	
	/**
	 * Test happy path (/user/1).
	 * 
	 * @throws JSONException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@Test
	public void test_User_Happy() throws JSONException, JsonParseException, JsonMappingException, IOException {
		log.traceEntry();
		ResponseEntity<ApiResponse> response = restTemplate.getForEntity(createURLWithPort("/user/1"),
				ApiResponse.class);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		final User responseUser = deserialiser.deserialiseToUser(getObjectMapFromResponse(response));
		final User expectedUser = new User("1", "deviceId1", "Oliver AWS");

		Assert.assertEquals(expectedUser, responseUser);

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
		ResponseEntity<ApiResponse> response = restTemplate.getForEntity(createURLWithPort("/user/100"),
				ApiResponse.class);

		Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		log.traceExit();
	}

	/**
	 * Test the endpoint /usersByDeviceId.
	 * 
	 * @throws JSONException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void test_UsersByDeviceId_Happy() throws JSONException, JsonParseException, JsonMappingException, IOException {
		log.traceEntry();
		final ResponseEntity<ApiResponse> response = restTemplate.getForEntity(createURLWithPort("/usersByDeviceId/deviceId1"),
				ApiResponse.class);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		final List<User> responseUser = deserialiser.deserialiseToUsers((List<User>) response.getBody().getObject());
		final User expectedUser = new User("1", "deviceId1", "Oliver AWS");

		Assert.assertEquals(expectedUser, responseUser.get(0));

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
	@SuppressWarnings("unchecked")
	@Test
	public void test_Tests_Happy() throws JSONException, JsonParseException, JsonMappingException, IOException {
		log.traceEntry();
		ResponseEntity<ApiResponse> response = restTemplate.getForEntity(createURLWithPort("/tests"),
				ApiResponse.class);

		final Question question = new Question(4, 4, "+");
		final List<Question> questions = new ArrayList<>();
		questions.add(question);

		final uk.co.olimor.BMBTApi_boot.model.Test expected = new uk.co.olimor.BMBTApi_boot.model.Test(1, "Reception",
				20, questions);

		List<uk.co.olimor.BMBTApi_boot.model.Test> tests = deserialiser.deserialiseJsonToTests(
				(List<uk.co.olimor.BMBTApi_boot.model.Test>) response.getBody().getObject());

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(1, tests.size());
		Assert.assertEquals(expected, tests.get(0));

		log.traceExit();
	}

	@Test
	public void testSubmitResult() {
		log.traceEntry();

		final TestResult result = new TestResult("1", 1, 5, 2, 10.5f);

		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		HttpEntity<TestResult> entity = new HttpEntity<TestResult>(result, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/submitResult"),
				entity, ApiResponse.class);

		Assert.assertEquals("Test Result submitted successfully", response.getBody().getConfirmationMessage());

		log.traceExit();
	}

	@Test
	public void testSubmitResult_Unknown_Test() {
		log.traceEntry();

		final TestResult result = new TestResult("1", 100, 5, 2, 10.5f);

		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		HttpEntity<TestResult> entity = new HttpEntity<TestResult>(result, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/submitResult"),
				entity, ApiResponse.class);

		Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

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
	public void test_ResultAnalysis_Happy()
			throws JSONException, JsonParseException, JsonMappingException, IOException {
		log.traceEntry();

		final ResponseEntity<ApiResponse> response = restTemplate.getForEntity(createURLWithPort("/resultsAnalysis/1"),
				ApiResponse.class);

		final ResultsAnalysis expected = new ResultsAnalysis();

		expected.setTotalTests(1);
		expected.setAverageAttemptedQuestions(7);
		expected.setAverageCorrectAnswers(5);
		expected.setTopCorrectAnswers(5);
		expected.setAverageTime(10.5f);
		expected.setBestTime(10.5f);

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(expected, deserialiser.deserialiseToResultsAnalysis(getObjectMapFromResponse(response)));

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
	public void test_ResultAnalysis_Unknown_User()
			throws JSONException, JsonParseException, JsonMappingException, IOException {
		log.traceEntry();

		final ResponseEntity<ApiResponse> response = restTemplate
				.getForEntity(createURLWithPort("/resultsAnalysis/100"), ApiResponse.class);

		Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

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
	public void test_ResultAnalysis_Null_User()
			throws JSONException, JsonParseException, JsonMappingException, IOException {
		log.traceEntry();

		final ResponseEntity<ApiResponse> response = restTemplate.getForEntity(createURLWithPort("/resultsAnalysis/"),
				ApiResponse.class);

		Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

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
	public void test_CreateUser_Happy() throws JSONException, JsonParseException, JsonMappingException, IOException {
		log.traceEntry();

		final CreateUserRequest user = new CreateUserRequest();
		
		user.setUserName("TestName");
		user.setDeviceId("deviceId");

		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		HttpEntity<CreateUserRequest> entity = new HttpEntity<CreateUserRequest>(user, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/createUser"),
				entity, ApiResponse.class);

		final String userId = (String) response.getBody().getObject();

		Assert.assertFalse(userId.isEmpty());

		log.traceExit();
	 }

	/**
	 * Create URL with port.
	 * 
	 * @param uri
	 *            - the endpoint to call.
	 * 
	 * @return - the full uri to call.
	 */
	private String createURLWithPort(String uri) {
		log.traceEntry();
		return log.traceExit("http://localhost:" + port + uri);
	}
	
	/**
	 * @param response - the response from the call to the API.
	 * 
	 * @return - the response Object as the Map<String, String> representation.
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getObjectMapFromResponse(final ResponseEntity<ApiResponse> response) {
		log.traceEntry();
		return log.traceExit((Map<String, String>) response.getBody().getObject());
	}

}
