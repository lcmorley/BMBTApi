package uk.co.olimor.BMBTApi_boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * Configuration for App.
 * 
 * @author leonmorley
 *
 */
@Configuration
public class AppConfig {

	/**
	 * Database connection.
	 * 
	 * @return the datasource.
	 */
	@Bean
	public MysqlDataSource getDataSource() {
		final MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUser("lcmorley");
		dataSource.setPassword("%lCm0rl3y50%");
		dataSource.setServerName("bmbtinstance.cabidcrldsms.us-east-1.rds.amazonaws.com");
		dataSource.setDatabaseName("bmbtschema");
		return dataSource;
	}
	
}
