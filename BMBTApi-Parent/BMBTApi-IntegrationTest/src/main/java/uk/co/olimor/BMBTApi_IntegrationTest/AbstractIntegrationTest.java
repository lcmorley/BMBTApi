package uk.co.olimor.BMBTApi_IntegrationTest;

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
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.common.Constants;
import uk.co.olimor.BMBTApi_Common.model.LoginCredentials;
import uk.co.olimor.BMBTApi_Common.model.Question;
import uk.co.olimor.BMBTApi_Common.model.TestAnalysis;
import uk.co.olimor.BMBTApi_Common.model.TestResult;
import uk.co.olimor.BMBTApi_Common.model.User;
import uk.co.olimor.BMBTApi_Common.requestmodel.CreateUserRequest;
import uk.co.olimor.BMBTApi_Common.response.ApiResponse;
import uk.co.olimor.BMBTApi_Common.security.JWTUtil;

/**
 * Class containing common tests.
 * 
 * @author leonmorley
 *
 */
@Log4j2
public abstract class AbstractIntegrationTest {

	/**
	 * {@link TestRestTemplate} instance.
	 */
	final RestTemplate restTemplate = new RestTemplate();

	/**
	 * Instance of the {@link JsonDeserialiser}.
	 */
	final JsonDeserialiser deserialiser = new JsonDeserialiser();

	/**
	 * Test Url.
	 */
	protected String testUrl;
	
	/**
	 * {@link JWTUtil} instance.
	 */
	protected JWTUtil util;

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
	protected DataSource datasource;

	/**
	 * Login username.
	 */
	protected String userName;
	
	/**
	 * Login password.
	 */
	protected String password;
	
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
		headers.set(Constants.NEW_DEVICE_HEADER, Base64.getEncoder().encodeToString("NEW_DEVICE.1".getBytes()));
		
		HttpEntity<LoginCredentials> entity = new HttpEntity<LoginCredentials>(login, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/registerDevice"),
				entity, ApiResponse.class);
		
		assertValue(HttpStatus.OK, response.getStatusCode(), "registerDevice");
				
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
		login.setPassword(password);
		login.setUserId(userName);

		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		
		HttpEntity<LoginCredentials> entity = new HttpEntity<LoginCredentials>(login, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/login"), entity,
				ApiResponse.class);

		assertValue(HttpStatus.OK, response.getStatusCode(), "login");
		
		if (response.getBody().getObject() == null) 
			throw new TestFailedException("login");

		final String token = (String) ((LinkedHashMap) response.getBody().getObject()).get("token");
		currentToken = token;

		final String subject = util.getSubjectFromToken(token);

		assertValue("1", subject, "login");

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

		final uk.co.olimor.BMBTApi_Common.model.Test expected = new uk.co.olimor.BMBTApi_Common.model.Test(1, "Reception",
				20, questions);

		List<uk.co.olimor.BMBTApi_Common.model.Test> tests = deserialiser
				.deserialiseJsonToTests((List<uk.co.olimor.BMBTApi_Common.model.Test>) response.getBody().getObject());

		assertValue(HttpStatus.OK, response.getStatusCode(), "tests");
		
		if (tests.size() == 0) 
			throw new TestFailedException("tests");

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

		assertValue(HttpStatus.OK, response.getStatusCode(), "createUser");

		final String userId = (String) response.getBody().getObject();
		
		if (userId.isEmpty()) 
			throw new TestFailedException("createUser");

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

		assertValue(HttpStatus.OK, response.getStatusCode(), "user");
		
		final User responseUser = deserialiser.deserialiseToUser(getObjectMapFromResponse(response));
		final User expectedUser = new User(currentUser, "1", "TestName");

		assertValue(expectedUser, responseUser, "user");

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

		assertValue(HttpStatus.OK, response.getStatusCode(), "usersByDeviceId");
		
		final List<User> responseUser = deserialiser.deserialiseToUsers((List<User>) response.getBody().getObject());
		final User expectedUser = new User(currentUser, "1", "TestName");

		assertValue(expectedUser, responseUser.get(0), "usersByDeviceId");

		log.traceExit();
	}

	public void submitResult() throws Exception {
		log.traceEntry();

		final TestResult result = new TestResult(currentUser, 1, 5, 2, 10.5f, "Full");

		HttpEntity<TestResult> entity = new HttpEntity<TestResult>(result, buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/submitResult"),
				entity, ApiResponse.class);

		assertValue(HttpStatus.OK, response.getStatusCode(), "submitResult");
		assertValue("Test Result submitted successfully", response.getBody().getConfirmationMessage(), "submitResult");

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

		assertValue(HttpStatus.OK, response.getStatusCode(), "resultAnalysis");
		assertValue(expected,
				deserialiser.deserialiseToTestAnalysisList((List<TestAnalysis>) response.getBody().getObject()), 
				"resultAnalysis");

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
		headers.add("x-jwt-token", currentToken);

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
		headers.add("x-jwt-token", "INVALID_JWT");

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

		assertValue(HttpStatus.FORBIDDEN, response.getStatusCode(), "createUser");
		assertValue(null, response.getBody().getObject(), "createUser");

		log.traceExit();
	}
	
	@Test
	public void test_Endpoint_Invalid_JWT_Unauthorised() throws Exception {
		log.traceEntry();

		registerDevice();
		login();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		headers.add("x-jwt-token", util.generateJWTToken("unknown"));
		
		HttpEntity entity = new HttpEntity(null, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/createUser"),
				entity, ApiResponse.class);

		assertValue(HttpStatus.FORBIDDEN, response.getStatusCode(), "createUser");
		assertValue(null, response.getBody().getObject(), "createUser");

		log.traceExit();
	}

	@Test
	public void test_Endpoint_Invalid_JWT_Internal_Server_Error() throws Exception {
		log.traceEntry();

		HttpEntity entity = new HttpEntity(null, buildHeaders_Invalid_JWT());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort(Constants.EMPTY_STRING),
				entity, ApiResponse.class);

		assertValue(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "createUser");
		assertValue(null, response.getBody().getObject(), "createUser");

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
		return log.traceExit(testUrl + uri);
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

	/**
	 * Test that actual and expected are the same.
	 * 
	 * @param expected
	 * @param actual
	 * @param api
	 * @throws TestFailedException
	 */
	protected void assertValue(final Object expected, final Object actual, final String api) 
			throws TestFailedException {
		if (expected == null && actual != null)
			throw new TestFailedException(api);
		
		if (expected == null && actual == null)
			return;
		
		if (!expected.equals(actual))
			throw new TestFailedException(api);		
	}
	
	
}
