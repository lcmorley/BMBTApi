package uk.co.olimor.bmbt_monitor_lambda;

import javax.sql.DataSource;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import uk.co.olimor.BMBTApi_Common.security.JWTUtil;
import uk.co.olimor.BMBTApi_IntegrationTest.BMBTApiIntegrationTest;

public class LambdaFunctionHandler implements RequestHandler<MonitorInput, String> {

	@Override
	public String handleRequest(MonitorInput monitorInput, Context context) {
		context.getLogger().log("Input: " + monitorInput);

		if (monitorInput == null)
			return "Failed";

		final ObjectMapper mapper = new ObjectMapper();

		try {
			final JWTUtil util = new JWTUtil();
			util.setSecret(monitorInput.getSecret());

			final boolean result = new BMBTApiIntegrationTest().runTest(monitorInput.getContextRoot(), getDataSource(monitorInput, context),
					util, context.getLogger(), monitorInput.getAppUserName(), monitorInput.getAppPassword());
			
			if (!result) {
				//create a new SNS client and set endpoint
				AmazonSNS snsClient = AmazonSNSClient.builder().withRegion(Regions.US_EAST_1).build();			
				snsClient.publish("arn:aws:sns:us-east-1:981596309454:BMBTMonitorState", "BMBT Monitor failed.");
			} 
			
			return result ? "Passed" : "Failed";

		} catch (final Exception e) {
			context.getLogger().log("An exception occurred whilst attempting to convert Json to Object." 
					+ monitorInput + ":" + e.getMessage());
		}

		return "Failed";
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
