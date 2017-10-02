package uk.co.olimor.BMBTApi_IntegrationTest;

import java.util.Base64;

import javax.sql.DataSource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.common.Constants;
import uk.co.olimor.BMBTApi_Common.model.LoginCredentials;
import uk.co.olimor.BMBTApi_Common.model.TestResult;
import uk.co.olimor.BMBTApi_Common.requestmodel.CreateUserRequest;
import uk.co.olimor.BMBTApi_Common.response.ApiResponse;
import uk.co.olimor.BMBTApi_Common.security.JWTUtil;

/**
 * Run test.
 *
 */
@Log4j2
public class BMBTApiIntegrationTest extends AbstractIntegrationTest {
	
	public boolean runTest(final String url, final DataSource datasource, final JWTUtil util) {
		log.entry();
		
		this.testUrl = url;
		this.datasource = datasource;
		this.util = util;
		
		try {			
			test_User_Journey_Happy();
			test_RegisterAttempt_Missing_Device_Id_In_Header_Info_Forbidden();
			test_RegisterAttempt_Incorrect_Key_Device_Id_In_Header_Info_Forbidden();
			test_RegisterAttempt_Missing_Header_Forbidden();
			test_LoginWithUnregisteredDevice_Unauthorised();
			test_LoginWithRegisteredDevice_Invalid_Creds_Unauthorised();
			test_CreateUser_Null_DeviceId();
			test_CreateUser_Invalid_DeviceId();
			test_CreateUser_Null_UserName();
			test_CreateUser_Empty_UserName();
			test_GetUser_UnknownUser();
			test_GetUsersByDeviceId_UnknownDevice();
			submitResult_unknown_user();
			submitResult_null_user();
			submitResult_unknown_test();
			submitResult_empty_user();
			submitResult_unknown_test();
			submitResult_empty_test();
			
			return log.exit(true);
		} catch (Exception e) {
			log.error("An exception occured during the test,", e);
			return log.exit(false);
		}
	}
	
	public void test_User_Journey_Happy() throws Exception {
		removeDevice();
		registerDevice();
		login();
		createUser();
		getUser();
		getUsersByDeviceId();
		getTests();
		submitResult();
		getResultsAnalysis();
	}
	
	public void test_RegisterAttempt_Missing_Device_Id_In_Header_Info_Forbidden() throws Exception {
		log.traceEntry();

		removeDevice();
		
		final LoginCredentials login = new LoginCredentials();

		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		headers.set(Constants.NEW_DEVICE_HEADER, Base64.getEncoder().encodeToString("NEW_DEVICE".getBytes()));
		
		HttpEntity<LoginCredentials> entity = new HttpEntity<LoginCredentials>(login, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/registerDevice"),
				entity, ApiResponse.class);

		assertValue(HttpStatus.FORBIDDEN, response.getStatusCode(), "registerDevice");

		log.traceExit();
	}

	public void test_RegisterAttempt_Incorrect_Key_Device_Id_In_Header_Info_Forbidden() throws Exception {
		log.traceEntry();

		removeDevice();
		
		final LoginCredentials login = new LoginCredentials();

		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		headers.set(Constants.NEW_DEVICE_HEADER, Base64.getEncoder().encodeToString("INCORRECT_KEY.1".getBytes()));
		
		HttpEntity<LoginCredentials> entity = new HttpEntity<LoginCredentials>(login, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/registerDevice"),
				entity, ApiResponse.class);

		assertValue(HttpStatus.FORBIDDEN, response.getStatusCode(), "registerDevice");

		log.traceExit();
	}

	public void test_RegisterAttempt_Missing_Header_Forbidden() throws Exception {
		log.traceEntry();
		
		removeDevice();

		final LoginCredentials login = new LoginCredentials();

		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		
		HttpEntity<LoginCredentials> entity = new HttpEntity<LoginCredentials>(login, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/registerDevice"),
				entity, ApiResponse.class);

		assertValue(HttpStatus.FORBIDDEN, response.getStatusCode(), "registerDevice");

		log.traceExit();
	}
	
	public void test_LoginWithUnregisteredDevice_Unauthorised() throws Exception {
		log.traceEntry();

		removeDevice();
		
		final LoginCredentials login = new LoginCredentials();

		login.setDeviceId("1");
		login.setPassword("testPassword");
		login.setUserId("testUser");

		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		
		HttpEntity<LoginCredentials> entity = new HttpEntity<LoginCredentials>(login, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/login"), entity,
				ApiResponse.class);

		assertValue(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "login");
		assertValue(null, response.getBody().getObject(), "login");
		
		log.traceExit();
	}

	public void test_LoginWithRegisteredDevice_Invalid_Creds_Unauthorised() throws Exception {
		log.traceEntry();
		
		removeDevice();
		
		registerDevice();
		
		final LoginCredentials login = new LoginCredentials();

		login.setDeviceId("1");
		login.setPassword("testPaword");
		login.setUserId("testUser");

		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", "application/json");
		
		HttpEntity<LoginCredentials> entity = new HttpEntity<LoginCredentials>(login, headers);

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/login"), entity,
				ApiResponse.class);

		assertValue(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "login");
		assertValue(null, response.getBody().getObject(), "login");
		
		log.traceExit();
	}
	
	public void test_CreateUser_Null_DeviceId() throws Exception {
		log.traceEntry();
		
		removeDevice();
		
		registerDevice();
		login();
		
		final CreateUserRequest user = new CreateUserRequest();

		user.setUserName("TestName");

		HttpEntity<CreateUserRequest> entity = new HttpEntity<CreateUserRequest>(user, buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/createUser"),
				entity, ApiResponse.class);

		assertValue(HttpStatus.BAD_REQUEST, response.getStatusCode(), "createUser");
		assertValue(null, response.getBody().getObject(), "createUser");
		
		log.traceExit();
	}

	public void test_CreateUser_Invalid_DeviceId() throws Exception {
		log.traceEntry();

		removeDevice();
		
		registerDevice();
		login();
		
		final CreateUserRequest user = new CreateUserRequest();

		user.setUserName("TestName");
		user.setDeviceId("INVALID");

		HttpEntity<CreateUserRequest> entity = new HttpEntity<CreateUserRequest>(user, buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/createUser"),
				entity, ApiResponse.class);

		assertValue(HttpStatus.NOT_FOUND, response.getStatusCode(), "createUser");
		assertValue(null, response.getBody().getObject(), "createUser");
		
		log.traceExit();
	}

	public void test_CreateUser_Null_UserName() throws Exception {
		log.traceEntry();

		removeDevice();
		
		registerDevice();
		login();
		
		final CreateUserRequest user = new CreateUserRequest();

		user.setDeviceId("INVALID");

		HttpEntity<CreateUserRequest> entity = new HttpEntity<CreateUserRequest>(user, buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/createUser"),
				entity, ApiResponse.class);

		assertValue(HttpStatus.BAD_REQUEST, response.getStatusCode(), "createUser");
		assertValue(null, response.getBody().getObject(), "createUser");
		
		log.traceExit();
	}

	public void test_CreateUser_Empty_UserName() throws Exception {
		log.traceEntry();

		removeDevice();
		
		registerDevice();
		login();
		
		final CreateUserRequest user = new CreateUserRequest();

		user.setDeviceId("INVALID");
		user.setUserName("");

		HttpEntity<CreateUserRequest> entity = new HttpEntity<CreateUserRequest>(user, buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/createUser"),
				entity, ApiResponse.class);

		assertValue(HttpStatus.BAD_REQUEST, response.getStatusCode(), "createUser");
		assertValue(null, response.getBody().getObject(), "createUser");
		
		log.traceExit();
	}
	
	public void test_GetUser_UnknownUser() throws Exception {
		log.traceEntry();
		
		removeDevice();
		
		registerDevice();
		login();
		createUser();
		
		HttpEntity entity = new HttpEntity(buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.exchange(createURLWithPort("/user/unknown"),
				HttpMethod.GET, entity, ApiResponse.class);

		assertValue(HttpStatus.NOT_FOUND, response.getStatusCode(), "user");
	}
	
	public void test_GetUsersByDeviceId_UnknownDevice() throws Exception {
		log.traceEntry();
		
		removeDevice();
		
		registerDevice();
		login();
		createUser();
		
		HttpEntity entity = new HttpEntity(buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.exchange(createURLWithPort("/usersByDeviceId/unknown"),
				HttpMethod.GET, entity, ApiResponse.class);

		assertValue(HttpStatus.NOT_FOUND, response.getStatusCode(), "usersByDeviceId");
	}
	
	public void submitResult_unknown_user() throws Exception {
		log.traceEntry();

		removeDevice();
		
		registerDevice();
		login();
		createUser();
		
		final TestResult result = new TestResult("unknown", 1, 5, 2, 10.5f, "Full");

		HttpEntity<TestResult> entity = new HttpEntity<TestResult>(result, buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/submitResult"),
				entity, ApiResponse.class);
		
		assertValue(HttpStatus.NOT_FOUND, response.getStatusCode(), "submitResult");		
		
		log.traceExit();
	}

	public void submitResult_null_user() throws Exception {
		log.traceEntry();

		removeDevice();
		
		registerDevice();
		login();
		createUser();
		
		final TestResult result = new TestResult(null, 1, 5, 2, 10.5f, "Full");

		HttpEntity<TestResult> entity = new HttpEntity<TestResult>(result, buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/submitResult"),
				entity, ApiResponse.class);

		assertValue(HttpStatus.BAD_REQUEST, response.getStatusCode(), "submitResult");
		
		log.traceExit();
	}

	public void submitResult_empty_user() throws Exception {
		log.traceEntry();

		removeDevice();
		
		registerDevice();
		login();
		createUser();
		
		final TestResult result = new TestResult(Constants.EMPTY_STRING, 1, 5, 2, 10.5f, "Full");

		HttpEntity<TestResult> entity = new HttpEntity<TestResult>(result, buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/submitResult"),
				entity, ApiResponse.class);

		assertValue(HttpStatus.BAD_REQUEST, response.getStatusCode(), "submitResult");
		
		log.traceExit();
	}
	
	public void submitResult_unknown_test() throws Exception {
		log.traceEntry();

		removeDevice();
		
		registerDevice();
		login();
		createUser();
		
		final TestResult result = new TestResult(currentUser, 30, 5, 2, 10.5f, "Full");

		HttpEntity<TestResult> entity = new HttpEntity<TestResult>(result, buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/submitResult"),
				entity, ApiResponse.class);

		assertValue(HttpStatus.NOT_FOUND, response.getStatusCode(), "submitResult");
		
		log.traceExit();
	}

	public void submitResult_empty_test() throws Exception {
		log.traceEntry();

		removeDevice();
		
		registerDevice();
		login();
		createUser();
		
		final TestResult result = new TestResult(currentUser, 0, 5, 2, 10.5f, "Full");

		HttpEntity<TestResult> entity = new HttpEntity<TestResult>(result, buildHeaders());

		final ResponseEntity<ApiResponse> response = restTemplate.postForEntity(createURLWithPort("/submitResult"),
				entity, ApiResponse.class);

		assertValue(HttpStatus.BAD_REQUEST, response.getStatusCode(), "submitResult");
		
		log.traceExit();
	}

}