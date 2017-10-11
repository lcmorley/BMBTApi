package uk.co.olimor.lambda.user_service.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.util.PropertiesUtil;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.common.Constants;

/**
 * Class used to load properties
 * 
 * @author leonmorley
 *
 */
@Log4j2
public class PropertyUtil {
	
	/**
	 * Properties.
	 */
	private static Properties properties;
	
	/**
	 * Get properties from the classpath.
	 * 
	 * @return - the built properties.
	 * 
	 * @throws IOException - exception that occurs when loading fails.
	 */
	private static Properties getProperties() throws IOException {
		log.traceEntry();
		
		if (properties == null) {
			log.info("Loading properties.");
			
			properties = new Properties();
			
			try (InputStream in = PropertiesUtil.class.getResourceAsStream("/application.properties")) {
				properties.load(in);	
			} catch (final IOException e) {
				log.error("An exception occurred whilst loading the properties.", e);
				throw e;
			}
		}
		
		return log.traceExit(properties);
	}
	
	/**
	 * Given the property name, retrieve the value.
	 * 
	 * @param propertyName - the property name.
	 * 
	 * @return - the property value.
	 */
	public static String getProperty(final String propertyName) {
		log.traceEntry(propertyName);
		
		try {
			return log.traceExit(getProperties().getProperty(propertyName));	
		} catch (final IOException e) {
			return log.traceExit(Constants.EMPTY_STRING);
		}				
	}

}
