package uk.co.olimor.bmbt_monitor_lambda;

import javax.sql.DataSource;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import uk.co.olimor.BMBTApi_Common.security.JWTUtil;
import uk.co.olimor.BMBTApi_IntegrationTest.BMBTApiIntegrationTest;

/**
 * Handler for the Lambda function.
 * 
 * @author leonmorley
 *
 */
public class LambdaFunctionHandler implements RequestHandler<MonitorInput, String> {

	/**
	 * Failed string.
	 */
	private static final String FAILED = "Failed";
	
	/**
	 * Passed string.
	 */
	private static final String PASSED = "Passed";
	
	@Override
	public String handleRequest(final MonitorInput monitorInput, final Context context) {
		context.getLogger().log("Input: " + monitorInput);

		if (monitorInput != null) {
			try {
				final JWTUtil util = new JWTUtil();
				util.setSecret(monitorInput.getSecret());
				 
				final boolean result = new BMBTApiIntegrationTest().runTest(monitorInput.getContextRoot(), 
						getDataSource(monitorInput, context), util, context.getLogger(), monitorInput.getAppUserName(), 
						monitorInput.getAppPassword());
				
				if (result) 
					return PASSED;			
			} catch (final Exception e) {
				context.getLogger().log("An exception occurred whilst running the integration test." 
						+ monitorInput + ":" + e.getMessage());
			}
		}
		
		return sendFailMessage();
	}

	/**
	 * Send fail message over SNS.
	 */
	private String sendFailMessage() {
		//create a new SNS client and set endpoint
		AmazonSNS snsClient = AmazonSNSClient.builder().withRegion(Regions.US_EAST_1).build();			
		snsClient.publish("arn:aws:sns:us-east-1:981596309454:BMBTMonitorState", "BMBT Monitor failed.");
		
		return FAILED;
	}
	
	/**
	 * Database {@link DataSource} bean configuration.
	 * 
	 * @return the datasource.
	 */
	public DataSource getDataSource(final MonitorInput input, final Context context) {
		final MysqlDataSource dataSource = new MysqlDataSource();

		dataSource.setUser(input.getDbUserName());
		dataSource.setPassword(input.getDbPassword());
		dataSource.setServerName(input.getConnectionString());
		dataSource.setDatabaseName(input.getSchemaName());
		
		return dataSource;
	}

}
