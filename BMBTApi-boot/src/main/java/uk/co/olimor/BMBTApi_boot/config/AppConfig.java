package uk.co.olimor.BMBTApi_boot.config;

import javax.activation.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import lombok.extern.log4j.Log4j2;

/**
 * Configuration for App.
 * 
 * @author leonmorley
 *
 */
@Configuration
@Log4j2
public class AppConfig {

	/**
	 * Database username.
	 */
	@Value("${userName}")
	private String userName;

	/**
	 * Database password.
	 */
	@Value("${password}")
	private String password;

	/**
	 * Database connection string.
	 */
	@Value("${connectionString}")
	private String connectionString;

	/**
	 * Databse schema name.
	 */
	@Value("${schema}")
	private String schema;

	/**
	 * Database {@link DataSource} bean configuration.
	 *  
	 * @return the datasource.
	 */
	@Bean
	public MysqlDataSource getDataSource() {
		log.traceEntry();
		
		log.info("Connecting to DB: " + connectionString + ", Schema: " + schema 
				+ " and user: " + userName);
		
		final MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUser(userName);
		dataSource.setPassword(password);
		dataSource.setServerName(connectionString);
		dataSource.setDatabaseName(schema);
		return log.traceExit(dataSource);
	}
	
}
