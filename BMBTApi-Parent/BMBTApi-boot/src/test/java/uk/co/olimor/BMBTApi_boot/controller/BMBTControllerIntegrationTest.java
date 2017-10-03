package uk.co.olimor.BMBTApi_boot.controller;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.security.JWTUtil;
import uk.co.olimor.BMBTApi_IntegrationTest.BMBTApiIntegrationTest;
import uk.co.olimor.BMBTApi_boot.Application;

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

	@Autowired
	private DataSource datasource;
	
	@Autowired
	private JWTUtil util;
	
	@Test
	public void runTest() {
		log.traceEntry();
		Assert.assertTrue(new BMBTApiIntegrationTest().runTest("http://localhost:" + port, datasource, util, null, 
				"testUser", "testPassword"));
		log.traceExit();
	}
	
}
