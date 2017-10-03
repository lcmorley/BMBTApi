package uk.co.olimor.bmbt_monitor_lambda;

import javax.sql.DataSource;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
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

			return new BMBTApiIntegrationTest().runTest(monitorInput.getContextRoot(), getDataSource(monitorInput, context),
					util, context.getLogger(), monitorInput.getAppUserName(), monitorInput.getAppPassword()) 
					? "Passed" : "Failed";

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
