package uk.co.olimor.bmbt_monitor_lambda;

import java.io.Serializable;

import lombok.Data;

@Data
public class MonitorInput implements Serializable {

	/**
	 * Default Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Connection String for the DB.
	 */
	private String connectionString;

	/**
	 * DB User name.
	 */
	private String dbUserName;

	/**
	 * DB Password.
	 */
	private String dbPassword;

	/**
	 * App Usewrname.
	 */
	private String appUserName;
	
	/**
	 * App Password.
	 */
	private String appPassword;
	
	/**
	 * Schema name.
	 */
	private String schemaName;

	/**
	 * Context Root.
	 */
	private String contextRoot;

	/**
	 * Secret.
	 */
	private String secret;

}
