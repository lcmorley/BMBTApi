package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.common.Constants;
import uk.co.olimor.BMBTApi_boot.dao.DeviceSecurityInsert;
import uk.co.olimor.BMBTApi_Common.exception.ApiException;

@Service
@Log4j2
public class DeviceSecurityInsertImpl extends AbstractUpdate<String> implements DeviceSecurityInsert {

	/**
	 * Insert statement.
	 */
	private static final String INSERT_STATEMENT = "INSERT INTO deviceSecurity VALUES ('%s','%s','%s')";
	
	@Override
	public void insertIntoDeviceSecurity(final String deviceId) throws ApiException {
		log.traceEntry(deviceId);
		update(deviceId);
		log.traceExit();
	}

	@Override
	protected String buildUpdate(final String deviceId) {
		log.traceEntry(deviceId);
		
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		final String currentTime = format.format(new Date());
		
		return log.traceExit(String.format(INSERT_STATEMENT, deviceId, Constants.EMPTY_STRING, 
				currentTime));
	}

}
