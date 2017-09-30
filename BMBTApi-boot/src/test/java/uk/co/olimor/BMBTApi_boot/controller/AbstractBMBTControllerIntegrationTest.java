package uk.co.olimor.BMBTApi_boot.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.Application;
import uk.co.olimor.BMBTApi_boot.JsonDeserialiser;
import uk.co.olimor.BMBTApi_boot.common.Constants;
import uk.co.olimor.BMBTApi_boot.model.LoginCredentials;
import uk.co.olimor.BMBTApi_boot.model.Question;
import uk.co.olimor.BMBTApi_boot.model.TestAnalysis;
import uk.co.olimor.BMBTApi_boot.model.TestResult;
import uk.co.olimor.BMBTApi_boot.model.User;
import uk.co.olimor.BMBTApi_boot.requestmodel.CreateUserRequest;
import uk.co.olimor.BMBTApi_boot.response.ApiResponse;
import uk.co.olimor.BMBTApi_boot.security.JWTUtil;

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
public abstract class AbstractBMBTControllerIntegrationTest {

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
	 * {@link JWTUtil} instance.
	 */
	@Autowired
	private JWTUtil util;

	/**
	 * Current token.
	 */
	private String currentToken;

	/**
	 * Current user.
	 */
	protected String currentUser;

	/**
	 * Datasource object.
	 */
	@Autowired
	protected DataSource datasource;

	@Before
	public void init() throws Exception {
		removeDevice();
	}

	public void removeDevice() throws Exception {
		Connection conn = null;
		Statement stmt = null;

		conn = datasource.getConnection();
		stmt = conn.createStatement();

		stmt.executeUpdate("DELETE FROM device WHERE deviceId = 1");

		conn.close();
		stmt.close();
	}

	/**
	 * Register Device.
	 */
	public void registerDevice() throws Exception {
		log.traceEntry();

		final LoginCredentials login = new LoginCredentials();

		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		headers.set(Constants.NEW_DEVICE, Base64.getEncoder().encodeToString("NEW_DEVICE.1".getBytes()));
		
		HttpEntity<LoginCredentials> entity = new HttpEntity<LoginCredentials>(login, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/registerDevice"),
				entity, ApiResponse.class);

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

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
	public void login() throws Exception {
		log.traceEntry();

		final LoginCredentials login = new LoginCredentials();

		login.setDeviceId("1");
		login.setPassword("testPassword");
		login.setUserId("testUser");

		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		
		HttpEntity<LoginCredentials> entity = new HttpEntity<LoginCredentials>(login, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/login"), entity,
				ApiResponse.class);

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull(response.getBody().getObject());

		final String token = (String) ((LinkedHashMap) response.getBody().getObject()).get("token");
		currentToken = token;

		final String subject = util.getSubjectFromToken(token);

		Assert.assertEquals("1", subject);

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
	public void getTests() throws Exception {
		log.traceEntry();

		HttpEntity entity = new HttpEntity(buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.exchange(createURLWithPort("/tests"), HttpMethod.GET,
				entity, ApiResponse.class);

		final Question question = new Question(4, 4, "+");
		final List<Question> questions = new ArrayList<>();
		questions.add(question);

		final uk.co.olimor.BMBTApi_boot.model.Test expected = new uk.co.olimor.BMBTApi_boot.model.Test(1, "Reception",
				20, questions);

		List<uk.co.olimor.BMBTApi_boot.model.Test> tests = deserialiser
				.deserialiseJsonToTests((List<uk.co.olimor.BMBTApi_boot.model.Test>) response.getBody().getObject());

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertTrue(tests.size() > 0);

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
	public void createUser() throws Exception {
		log.traceEntry();

		final CreateUserRequest user = new CreateUserRequest();

		user.setUserName("TestName");
		user.setDeviceId("1");

		HttpEntity<CreateUserRequest> entity = new HttpEntity<CreateUserRequest>(user, buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/createUser"),
				entity, ApiResponse.class);

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

		final String userId = (String) response.getBody().getObject();
		Assert.assertFalse(userId.isEmpty());

		currentUser = userId;

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
	public void getUser() throws Exception {
		log.traceEntry();

		HttpEntity entity = new HttpEntity(buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.exchange(createURLWithPort("/user/" + currentUser),
				HttpMethod.GET, entity, ApiResponse.class);

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		final User responseUser = deserialiser.deserialiseToUser(getObjectMapFromResponse(response));
		final User expectedUser = new User(currentUser, "1", "TestName");

		Assert.assertEquals(expectedUser, responseUser);

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
	public void getUsersByDeviceId() throws Exception {
		log.traceEntry();

		HttpEntity entity = new HttpEntity(buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.exchange(createURLWithPort("/usersByDeviceId/1"),
				HttpMethod.GET, entity, ApiResponse.class);

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		final List<User> responseUser = deserialiser.deserialiseToUsers((List<User>) response.getBody().getObject());
		final User expectedUser = new User(currentUser, "1", "TestName");

		Assert.assertEquals(expectedUser, responseUser.get(0));

		log.traceExit();
	}

	public void submitResult() {
		log.traceEntry();

		final TestResult result = new TestResult(currentUser, 1, 5, 2, 10.5f, "Full");

		HttpEntity<TestResult> entity = new HttpEntity<TestResult>(result, buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/submitResult"),
				entity, ApiResponse.class);

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals("Test Result submitted successfully", response.getBody().getConfirmationMessage());

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
	@SuppressWarnings("unchecked")
	public void getResultsAnalysis() throws Exception {
		log.traceEntry();

		HttpEntity entity = new HttpEntity(buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.exchange(createURLWithPort("/resultsAnalysis/user/" 
				+ currentUser), HttpMethod.GET, entity, ApiResponse.class);

		final List<TestAnalysis> expected = new ArrayList<>();

		TestAnalysis analysis = new TestAnalysis(1);
		analysis.setTotalTests(1);
		analysis.setAverageAttemptedQuestions(7);
		analysis.setAverageCorrectAnswers(5);
		analysis.setTopCorrectAnswers(5);
		analysis.setAverageTime(10.5f);
		analysis.setBestTime(10.5f);

		expected.add(analysis);

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(expected,
				deserialiser.deserialiseToTestAnalysisList((List<TestAnalysis>) response.getBody().getObject()));

		log.traceExit();
	}

	/**
	 * Build and return the {@link HttpHeaders} object.
	 * 
	 * @return
	 */
	protected HttpHeaders buildHeaders() {
		log.entry();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		headers.add("JWT_TOKEN", currentToken);

		return log.exit(headers);
	}

	/**
	 * Build and return the {@link HttpHeaders} object.
	 * 
	 * @return
	 */
	protected HttpHeaders buildHeaders_Invalid_JWT() {
		log.entry();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		headers.add("JWT_TOKEN", "INVALID_JWT");

		return log.exit(headers);
	}
	
	/**
	 * Build and return the {@link HttpHeaders} object.
	 * 
	 * @return
	 */
	protected HttpHeaders buildHeadersNoJWT() {
		log.entry();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");

		return log.exit(headers);
	}

	@Test
	public void test_Endpoint_Not_Logged_In_Forbidden() throws Exception {
		log.traceEntry();

		HttpEntity entity = new HttpEntity(null, buildHeadersNoJWT());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/createUser"),
				entity, ApiResponse.class);

		Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		Assert.assertNull(response.getBody().getObject());

		log.traceExit();
	}
	
	@Test
	public void test_Endpoint_Invalid_JWT_Unauthorised() throws Exception {
		log.traceEntry();

		registerDevice();
		login();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		headers.add("JWT_TOKEN", util.generateJWTToken("unknown"));
		
		HttpEntity entity = new HttpEntity(null, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/createUser"),
				entity, ApiResponse.class);

		Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		Assert.assertNull(response.getBody().getObject());

		log.traceExit();
	}

	@Test
	public void test_Endpoint_Invalid_JWT_Internal_Server_Error() throws Exception {
		log.traceEntry();

		HttpEntity entity = new HttpEntity(null, buildHeaders_Invalid_JWT());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort(Constants.EMPTY_STRING),
				entity, ApiResponse.class);

		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		Assert.assertNull(response.getBody().getObject());

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
	protected String createURLWithPort(String uri) {
		log.traceEntry();
		return log.traceExit("http://localhost:" + port + uri);
	}

	/**
	 * @param response
	 *            - the response from the call to the API.
	 * 
	 * @return - the response Object as the Map<String, String> representation.
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getObjectMapFromResponse(final ResponseEntity<ApiResponse> response) {
		log.traceEntry();
		return log.traceExit((Map<String, String>) response.getBody().getObject());
	}

}
